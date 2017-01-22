package com.mycompany.service;

import com.mycompany.domain.User;
import com.mycompany.utils.PageBean;

public interface UserService {

	public void editPassword(String password, String id);

	public User findUserByUsername(String username);

	public void pageQuery(PageBean pageBean);

	public void save(User model, String[] roleIds);

}
