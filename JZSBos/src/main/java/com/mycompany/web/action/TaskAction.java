package com.mycompany.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.mycompany.domain.Workordermanage;
import com.mycompany.service.WorkordermanageService;
import com.mycompany.utils.BOSContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


@Controller
@Scope("prototype")
@Namespace("/task")
@Results({@Result(name="tppersonaltasklist",type="redirectAction",location="findPersonalTask")})
public class TaskAction extends ActionSupport{

	private static final long serialVersionUID = -1837514604868078159L;
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private WorkordermanageService workordermanageService;
	
	/**
	 * 查询组任务
	 * @return
	 */
	@Action(value="findGroupTask",results={@Result(name="grouptasklist",location = "../workflow/grouptask.jsp")})
	public String findGroupTask(){
		TaskQuery query = taskService.createTaskQuery();
		String candidateUser = BOSContext.getLoginUser().getId();
		//组任务过滤
		query.taskCandidateUser(candidateUser);
		List<Task> list = query.list();
		ActionContext.getContext().getValueStack().set("list", list);
		return "grouptasklist";
	}
	
	private String taskId;
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 拾取组任务
	 * @return
	 */
	@Action(value="takeTask",results={@Result(name="gogrouptasklist",location="findGroupTask",type="redirectAction")})
	public String takeTask(){
		String userId = BOSContext.getLoginUser().getId();
		taskService.claim(taskId, userId);
		return "gogrouptasklist";
	}
	
	/**
	 * 根据任务id查询对应的流程变量数据 
	 * @return
	 * @throws IOException
	 */
	@Action("showData")
	public String showData() throws IOException{
		Map<String, Object> variables = taskService.getVariables(taskId);
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(variables.toString());
		return NONE;
	}
	
	/**
	 * 查询个人办理任务
	 * @return
	 */
	@Action(value="findPersonalTask",results={@Result(name="personaltasklist",location="../workflow/personaltask.jsp")})
	public String findPersonalTask(){
		TaskQuery query = taskService.createTaskQuery();
		String assignee = BOSContext.getLoginUser().getId();
		//个人任务过滤
		query.taskAssignee(assignee);
		List<Task> list = query.list();
		ActionContext.getContext().getValueStack().set("list", list);
		return "personaltasklist";
	}
	
	private Integer check;//审核结果，0：审核不通过 1:审核通过
	public void setCheck(Integer check) {
		this.check = check;
	}
	
	/**
	 * 办理审核工作单任务
	 * @return
	 */
	@Action(value="checkWorkOrderManage",results={@Result(name="check",location="../workflow/check.jsp")})
	public String checkWorkOrderManage(){
		// 根据任务id查询任务对象
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务对象查询流程实例id
		String processInstanceId = task.getProcessInstanceId();
		// 根据流程实例id查询流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		String workordermanageId = processInstance.getBusinessKey();
		Workordermanage workordermanage = workordermanageService.findById(workordermanageId);
		if(check == null){
			//跳转到审核页面
			//跳转到一个审核工作单页面，展示当前 对应的工作单信息
			ActionContext.getContext().getValueStack().set("map", workordermanage);
			ActionContext.getContext().getValueStack().set("taskId", taskId);
			return "check";
		}else{
			workordermanageService.checkWorkordermange(taskId,check,workordermanageId);
			return "tppersonaltasklist";
		}
	}
	
	/**
	 * 办理出库任务
	 * @return
	 */
	@Action("outStore")
	public String outStore(){
		taskService.complete(taskId);
		return "tppersonaltasklist";
	}
	
	/**
	 * 办理配送任务
	 * @return
	 */
	@Action("transferGoods")
	public String transferGoods(){
		taskService.complete(taskId);
		return "tppersonaltasklist";
	}
	
	/**
	 * 办理签收任务
	 * @return
	 */
	@Action("receive")
	public String receive(){
		taskService.complete(taskId);
		return "tppersonaltasklist";
	}
}
