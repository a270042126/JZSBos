package com.mycompany.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.Workordermanage;
import com.mycompany.service.WorkordermanageService;
import com.mycompany.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/workordermanage")
@Results({  
	@Result(name="list",location = "../workflow/startransfer.jsp"),
	@Result(name="toList",location="list",type="redirectAction")
}) 
public class WorkordermanageAction extends BaseAction<Workordermanage>{

	private static final long serialVersionUID = -8900001973332714843L;
	
	@Autowired
	private WorkordermanageService workordermanageService;
	
	/**
	 * 查询start为0的工作单
	 * @return
	 */
	@Action("list")
	public String list(){
		List<Workordermanage> list = workordermanageService.findListNotStart();
		ActionContext.getContext().getValueStack().set("list", list);
		return "list";
	}
	
	/**
	 * 启动流程实例
	 * @return
	 */
	@Action("start")
	public String start(){
		String id = model.getId();//工作单id
		workordermanageService.start(id); //启动流程实例
		return "toList";
	}
	
	/**
	 * 添加工作单
	 * @return
	 * @throws IOException
	 */
	@Action("add")
	public String add() throws IOException{
		String flag = "1";
		try {
			workordermanageService.save(model);
		} catch (Exception e) {
			flag = "0";
		}
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(flag);
		return NONE;
	}

}
