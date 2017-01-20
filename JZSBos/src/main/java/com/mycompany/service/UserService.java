package com.mycompany.service;

import com.mycompany.domain.User;

public interface UserService {

	public void editPassword(String password, String id);

	public User findUserByUsername(String username);

}
