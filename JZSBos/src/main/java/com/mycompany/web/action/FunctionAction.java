package com.mycompany.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.Function;
import com.mycompany.service.FunctionService;
import com.mycompany.web.action.base.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/function")
public class FunctionAction extends BaseAction<Function>{

	private static final long serialVersionUID = -3508723737518020714L;

	@Autowired
	private FunctionService functionService;
	
	private List<Function> functionList;
	public List<Function> getFunctionList() {
		return functionList;
	}
	
	@Action(value="listajax",results={@Result(type="json",
			params={"root","functionList","excludeProperties",".*\\.function,.*\\.functions,.*\\.roles"})})
	public String listajax(){
		functionList = functionService.findAll();
		return SUCCESS; 
	}
}
