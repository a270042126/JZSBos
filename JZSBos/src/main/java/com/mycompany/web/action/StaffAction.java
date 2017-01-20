package com.mycompany.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.Staff;
import com.mycompany.service.StaffService;
import com.mycompany.web.action.base.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/staff")
@Results({
	@Result(name = "list",location = "../base/staff.jsp")
})
public class StaffAction extends BaseAction<Staff>{

	private static final long serialVersionUID = -2082225006777091053L;
	
	@Autowired
	public StaffAction(StaffService staffService) {
		this.staffService = staffService;
	}
	
	private StaffService staffService;
	
	/**
	 * 添加取派员
	 */
	@Action("add")
	public String add(){
		staffService.save(model);
		return "list";
	}

	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	@Action(value="pageQuery",results={@Result(type="json",
			params={"root","pageBean","excludeProperties","rows.*\\.decidedzones,detachedCriteria"})})
	public String pageQuery(){
		// 在查询之前，封装条件
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		String name = model.getName();
		String telephone = model.getTelephone();
		String station = model.getStation();
		String standard = model.getStandard();
		
		if(StringUtils.isNotBlank(name)){
			detachedCriteria.add(Restrictions.like("name", "%" + name + "%"));
		}
		
		if(StringUtils.isNotBlank(telephone)){
			detachedCriteria.add(Restrictions.like("telephone", "%" + telephone + "%"));
		}
		
		if(StringUtils.isNotBlank(station)){
			detachedCriteria.add(Restrictions.like("station", "%" + station + "%"));
		}
		
		if(StringUtils.isNotBlank(standard)){
			detachedCriteria.add(Restrictions.like("station", "%" + standard + "%"));
		}
		
		staffService.pageQuery(pageBean);
		return SUCCESS;
	}
	
	/**
	 * 修改取派员信息
	 */
	@Action("edit")
	public String edit(){
		//获得当前用户
//		Subject subject = SecurityUtils.getSubject();
//		subject.checkPermission("abcd");
		
		//显查询数据库中原始数据
		Staff staff = staffService.findById(model.getId());
		//再按照页面提交的参数进行覆盖
		staff.setName(model.getName());
		staff.setTelephone(model.getTelephone());
		staff.setStation(model.getStation());
		staff.setHaspda(model.getHaspda());
		staff.setStandard(model.getStandard());
		
		staffService.update(staff);
		
		return "list";
	}
	
	//接收ids参数
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 批量删除功能（逻辑删除）
	 * @return
	 */
	@Action("delete")
	@RequiresPermissions(value="staff")//执行当前方法需要具有staff权限
//	@RequiresRoles(value="abc")
	public String delete(){
		staffService.deleteBatch(ids);
		return "list";
	}
	
	/**
	 * 批量还原功能
	 * @return
	 */
	@Action("restore")
	@RequiresPermissions(value="staff")//执行当前方法需要具有staff权限
	public String restore(){
		staffService.restore(ids);
		return "list";
	}
	
	private List<Staff> staffList = null;
	public List<Staff> getStaffList() {
		return staffList;
	}
	/**
	 * 查询没有作废的取派员，返回json
	 */
	@Action(value="listajax",results={@Result(type="json",
			params={"root","staffList","excludeProperties",".*\\.decidedzones"})})
	public String listajax(){
		staffList = staffService.findListNotDelete();
		return SUCCESS;
	}
}
