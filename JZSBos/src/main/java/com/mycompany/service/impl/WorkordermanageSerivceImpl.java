package com.mycompany.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.WorkordermanageDao;
import com.mycompany.domain.Workordermanage;
import com.mycompany.service.WorkordermanageService;

@Service
@Transactional
public class WorkordermanageSerivceImpl implements WorkordermanageService{

	@Autowired
	private WorkordermanageDao workordermanageDao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;

	public List<Workordermanage> findListNotStart() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Workordermanage.class);
		detachedCriteria.add(Restrictions.eq("start", "0"));
		return workordermanageDao.findByCriteria(detachedCriteria);
	}

	public void start(String id) {
		Workordermanage workordermanage = workordermanageDao.findById(id);
		workordermanage.setStart("1");//设置启动
		String processDefinitionKey = "transfer";//流程定义key
		String bussinessKey = id;//业务主键----等于业务表（工作单）主键值---让工作流框架找到业务数据 
		Map<String, Object> variables = new HashMap<String, Object>();//流程变量
		variables.put("业务数据", workordermanage);
		runtimeService.startProcessInstanceByKey(processDefinitionKey, bussinessKey, variables);
	}

	public void save(Workordermanage model) {
		model.setUpdatetime(new Date());
		workordermanageDao.save(model);
	}

	public Workordermanage findById(String workordermanageId) {
		return workordermanageDao.findById(workordermanageId);
	}

	public void checkWorkordermange(String taskId, Integer check, String workordermanageId) {
	
		Workordermanage workordermanage = workordermanageDao.findById(workordermanageId);
		//查询流程实例
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		Map<String, Object> variables = new  HashMap<String, Object>();
		variables.put("check", check);
		//办理审核 工作单任务
		taskService.complete(taskId,variables);
		//如果审核不通过,删除历史流程实例数据
		if(check == 0){
			workordermanage.setStart("0");
			String processInstanceId = task.getProcessInstanceId();
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}
	}
}
