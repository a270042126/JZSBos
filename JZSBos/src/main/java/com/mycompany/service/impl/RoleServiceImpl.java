package com.mycompany.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mycompany.dao.RoleDao;
import com.mycompany.domain.Function;
import com.mycompany.domain.Role;
import com.mycompany.service.RoleService;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	@Resource
	private IdentityService identityService;

	public List<Role> findAll() {
		return roleDao.findAll();
	}

	public void pageQuery(PageBean pageBean) {
		roleDao.pageQuery(pageBean);
	}

	/**
	 * 保存一个角色，同步到activiti的act_id_group组表中
	 */
	public void save(Role model, String ids) {
		roleDao.save(model);//持久对象
		
		//将角色同步到act_id_group表
		//使用角色的名称作为组的id
		Group group = new GroupEntity(model.getName());
		identityService.saveGroup(group);
		
		String[] functionIds = ids.split(",");
		for (String fid : functionIds) {
			Function function = new Function(fid);//托管,离线对象
			//角色关联权限
			model.getFunctions().add(function);
		}
	}
	
	
}
