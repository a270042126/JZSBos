package com.mycompany.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.crm.domain.Customer;
import com.mycompany.crm.service.CustomerService;
import com.mycompany.domain.Noticebill;
import com.mycompany.domain.User;
import com.mycompany.service.NoticebillService;
import com.mycompany.utils.BOSContext;
import com.mycompany.web.action.base.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/noticebill")
@Results({
	@Result(name = "addUI",location = "../qupai/noticebill_add.jsp")
})
public class NoticebillAction extends BaseAction<Noticebill>{

	private static final long serialVersionUID = 1755661271267053545L;
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private NoticebillService noticebillService;
	/**
	 * 调用代理对象，根据手机号查询客户信息
	 */
	private Customer customer;
	public Customer getCustomer() {
		return customer;
	}
	@Action(value="findCustomerByTelephone",results={@Result(type="json",
			params={"root","customer"})})
	public String findCustomerByTelephone(){
		customer = customerService.findCustomerByPhonenumber(model.getTelephone());
		return SUCCESS;
	}

	/**
	 * 保存业务通知单，尝试自动分单
	 * @return
	 */
	@Action("add")
	public String add(){
		User user = BOSContext.getLoginUser();
		model.setUser(user);
		noticebillService.save(model);
		return "addUI";
	}
}
