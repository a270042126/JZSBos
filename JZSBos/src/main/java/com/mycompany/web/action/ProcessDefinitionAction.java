package com.mycompany.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程定义管理
 * 
 */
@Controller
@Scope("prototype")
@Namespace("/processDefinition")
@Results({
	@Result(name = "list",location = "../workflow/processdefinition_list.jsp"),
	@Result(name="toList",location="list",type="redirectAction")
})
public class ProcessDefinitionAction extends ActionSupport{

	private static final long serialVersionUID = -6881184214020969473L;
	
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 查询最新版本流程定义列表数据
	 */
	
	@Action("list")
	public String list(){
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		query.latestVersion();// 查询最新版本
		query.orderByProcessDefinitionName().desc();// 排序
		List<ProcessDefinition> list = query.list();//执行查询
		//压栈
		ActionContext.getContext().getValueStack().set("list", list);
		return "list";
	}

	private File zipFile;
	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}
	
	/**
	 * 部署流程定义
	 * @throws FileNotFoundException 
	 */
	@Action("deploy")
	public String deploy() throws FileNotFoundException{
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addZipInputStream(new ZipInputStream(new FileInputStream(zipFile)));
		deploymentBuilder.deploy();
		return "toList";
	}

	//接收流程定义id
	private String id;
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 展示png图片
	 */
	@Action(value="showpng",results={@Result(name="showpng",type="stream",
			params={"contentType","image/png","inputName","pngStream"})})
	public String showpng(){
		//获取png图片对应的输入流
		InputStream pngStream = repositoryService.getProcessDiagram(id);
		ActionContext.getContext().getValueStack().set("pngStream", pngStream);
		return "showpng";
	}
	
	/**
	 * 删除流程定义
	 */
	@Action("delete")
	public String delete(){
		String deltag = "0";
		//根据流程定义id查询部署id
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		query.processDefinitionId(id);//根据id过滤
		ProcessDefinition processDefinition = query.singleResult();
		String deploymentId = processDefinition.getDeploymentId();
		try {
			repositoryService.deleteDeployment(deploymentId);
		} catch (Exception e) {
			//当前要删除的流程定义正在使用
			deltag = "1";
			ActionContext.getContext().getValueStack().set("deltag", deltag);
			ProcessDefinitionQuery query2 = repositoryService.createProcessDefinitionQuery();
			query2.latestVersion();// 查询最新版本
			query2.orderByProcessDefinitionName().desc();// 排序
			List<ProcessDefinition> list = query2.list();// 执行查询
			// 压栈
			ActionContext.getContext().getValueStack().set("list", list);
			return "list";
		}
		return "toList";
	}
}
