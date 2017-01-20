package com.mycompany.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.crm.domain.Customer;
import com.mycompany.crm.service.CustomerService;
import com.mycompany.domain.Decidedzone;
import com.mycompany.domain.Staff;
import com.mycompany.service.DecidedzoneService;
import com.mycompany.web.action.base.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/decidedzone")
@Results({
	@Result(name = "list",location = "../base/decidedzone.jsp")
})
public class DecidedzoneAction extends BaseAction<Decidedzone>{

	private static final long serialVersionUID = -7874803913175316277L;

	@Autowired
	private DecidedzoneService decidedzoneService;
	@Autowired
	private CustomerService customerService;

	
	// 接收分区id
	private String[] subareaid;
	public void setSubareaid(String[] subareaid) {
		this.subareaid = subareaid;
	}
	
	/**
	 * 添加定区
	 * 
	 * @return                              
	 */
	@Action("add")
	public String add(){
		decidedzoneService.save(model,subareaid);
		return "list";
	}
	
	/**
	 * 分页查询方法
	 */
	@Action(value="pageQuery",results={@Result(type="json",
			params={"root","pageBean","excludeProperties","detachedCriteria,rows.*\\.decidedzones,rows.*\\.decidedzone"})})
	public String pageQuery(){
		// 在查询之前，封装条件
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		String id = model.getId();
		Staff staff = model.getStaff();
		
		if(StringUtils.isNotBlank(id)){
			detachedCriteria.add(Restrictions.like("id", "%" +id  + "%"));
		}
	
		if(staff != null){
			// 创建别名，用于多表关联查询
			detachedCriteria.createAlias("staff", "s");
			String station = staff.getStation();
			if(StringUtils.isNotBlank(station)){
				detachedCriteria.add(Restrictions.like("s.station", "%" + station + "%"));
			}
		}
		
		decidedzoneService.pageQuery(pageBean);
		return SUCCESS;
	}
	
	@Action("edit")
	public String edit(){
		Decidedzone decidedzone = decidedzoneService.findById(model.getId());
		decidedzone.setName(model.getName());
		decidedzone.setStaff(model.getStaff());
		
		decidedzoneService.update(decidedzone,subareaid);
		return "list";
	}
	
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action("delete")
	public String delete(){
		decidedzoneService.deleteByIds(ids);
		return "list";
	}
	
	private List<Customer> noassociationCustomerList;
	public List<Customer> getNoassociationCustomerList() {
		return noassociationCustomerList;
	}
	/**
	 * 查询没有关联到定区的客户
	 * @return
	 */
	@Action(value="findnoassociationCustomers",results={@Result(type="json",
			params={"root","noassociationCustomerList",
					"excludeProperties",".*\\.station,.*\\.address"})})
	public String findnoassociationCustomers(){
		noassociationCustomerList = customerService.findnoassociationCustomers();
		return SUCCESS;
	}
	
	private List<Customer> hasassociationCustomerList;
	public List<Customer> getHasassociationCustomerList() {
		return hasassociationCustomerList;
	}
	/**
	 * 查询已经关联到指定定区的客户
	 * @return
	 */
	@Action(value="findhasassociationCustomers",results={@Result(type="json",
			params={"root","hasassociationCustomerList",
					"excludeProperties",".*\\.station,.*\\.address"})})
	public String findhasassociationCustomers(){
		hasassociationCustomerList = customerService.findhasassociationCustomers(model.getId());
		return SUCCESS;
	}
	
	private Integer[] customerIds;
	public void setCustomerIds(Integer[] customerIds) {
		this.customerIds = customerIds;
	}
	/**
	 * 定区关联客户
	 * @return
	 */
	@Action("assigncustomerstodecidedzone")
	public String assigncustomerstodecidedzone(){
		customerService.assignCustomersToDecidedZone(customerIds, model.getId());
		return "list";
	}
}
