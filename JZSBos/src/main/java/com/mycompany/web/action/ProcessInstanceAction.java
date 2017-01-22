package com.mycompany.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
@Namespace("/processInstance")
@Results({
	@Result(name = "list",location = "../workflow/processinstance.jsp")
})
public class ProcessInstanceAction extends ActionSupport{

	private static final long serialVersionUID = -1272881362646793466L;
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	
	/** 
	 * 查询流程实例列表数据 
	 * @return
	 */
	@Action("list")
	public String list(){
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		query.orderByProcessInstanceId().desc();
		List<ProcessInstance> list = query.list();
		ActionContext.getContext().getValueStack().set("list", list);
		return "list";
	}
	
	private String id;
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 查看正在运行的流程实例
	 * @return
	 * @throws IOException
	 */
	@Action("findData")
	public String findData() throws IOException{
		Map<String, Object> variables =  runtimeService.getVariables(id);
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(variables.toString());
		return NONE;
	}
	
	private String deploymentId;
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	
	private String imageName;
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	@Action(value="showPng",results={@Result(name="showPng",location="../workflow/image.jsp")})
	public String showPng(){
		//1、根据流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
		//2、根据流程实例对象查询流程定义
		String processDefinitionId = processInstance.getProcessDefinitionId();
		//3、根据流程定义id对象查询
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		//4、根据流程定义对象查询部署id
		deploymentId = processDefinition.getDeploymentId();
		imageName = processDefinition.getDiagramResourceName();
		
		//查询坐标
		//1、获得当前 流程实例执行到哪个节点
		String activityId = processInstance.getActivityId();
		//2、加载bpmn(xml)文件，获得一个流程定义对象
		//查询 act_ge_bytearray
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//3、根据activitiId获取含有坐标信息的对象
		ActivityImpl findActivity = pd.findActivity(activityId);
		int x = findActivity.getX();
		int y = findActivity.getY();
		int width = findActivity.getWidth();
		int height = findActivity.getHeight();
		
		ActionContext.getContext().getValueStack().set("x", x);
		ActionContext.getContext().getValueStack().set("y", y);
		ActionContext.getContext().getValueStack().set("width", width);
		ActionContext.getContext().getValueStack().set("height", height);
		ActionContext.getContext().getValueStack().set("deploymentId", deploymentId);
		ActionContext.getContext().getValueStack().set("imageName", imageName);
		return "showPng";
	}
	
	private InputStream pngStream;
	public InputStream getPngStream() {
		return pngStream;
	}
	
	@Action(value="viewImage",results={@Result(name="viewImage",type="stream",
			params={"contentType","image/png","inputName","pngStream"})})
	public String viewImage(){
		pngStream = repositoryService.getResourceAsStream(deploymentId, imageName);
		return "viewImage";
	}
}
