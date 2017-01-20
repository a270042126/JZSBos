package com.mycompany.dao;

import com.mycompany.dao.base.BaseDao;
import com.mycompany.domain.User;

public interface UserDao extends BaseDao<User> {

	public User findUserByUsername(String username);

}
