package com.mycompany.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.UserDao;
import com.mycompany.domain.User;
import com.mycompany.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	public void editPassword(String password, String id) {
		userDao.executeUpdate("editPassword", password,id);
	}

	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

}
