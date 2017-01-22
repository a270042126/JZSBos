package com.mycompany.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.Role;
import com.mycompany.service.RoleService;
import com.mycompany.web.action.base.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/role")
@Results({  
	@Result(name="list",location = "../admin/role.jsp")
}) 
public class RoleAction extends BaseAction<Role>{

	private static final long serialVersionUID = -3624631682568568311L;
	
	@Autowired
	private RoleService roleService;
	
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action("add")
	public String add(){
		roleService.save(model,ids);
		return "list";
	}
	
	@Action(value="pageQuery",results={@Result(type="json",
			params={"root","pageBean","excludeProperties","rows.*\\.functions,rows.*\\.users,detachedCriteria"})})
	public String pageQuery(){
		roleService.pageQuery(pageBean);
		return SUCCESS;
	}
	
	List<Role> roleList;
	public List<Role> getRoleList() {
		return roleList;
	}
	
	@Action(value="listajax",results={@Result(type="json",
			params={"root","roleList","excludeProperties",".*\\.functions,.*\\.users"})})
	public String listajax(){
		roleList = roleService.findAll();
		return SUCCESS;
	}
}
