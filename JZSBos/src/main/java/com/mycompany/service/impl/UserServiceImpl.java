package com.mycompany.service.impl;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.RoleDao;
import com.mycompany.dao.UserDao;
import com.mycompany.domain.Role;
import com.mycompany.domain.User;
import com.mycompany.service.UserService;
import com.mycompany.utils.MD5Utils;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Resource
	private IdentityService identityService;
	
	public void editPassword(String password, String id) {
		userDao.executeUpdate("editPassword", password,id);
	}

	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

	public void pageQuery(PageBean pageBean) {
		userDao.pageQuery(pageBean);
	}

	/**
	 * 保存一个用户，同步到activiti的act_id_user、act_id_menbership
	 */
	public void save(User model, String[] roleIds) {
		model.setPassword(MD5Utils.md5(model.getPassword()));
		userDao.save(model);
		
		//将用户同步到act_id_user表
		org.activiti.engine.identity.User actUser = new UserEntity(model.getId());
		identityService.saveUser(actUser);
				
		for(String roleId: roleIds){
			Role role = roleDao.findById(roleId);
			model.getRoles().add(role);
			
			identityService.createMembership(actUser.getId(), role.getName());
		}
	}

}
