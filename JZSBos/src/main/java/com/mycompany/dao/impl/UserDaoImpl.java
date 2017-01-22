package com.mycompany.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.UserDao;
import com.mycompany.dao.base.impl.BaseDaoImpl;
import com.mycompany.domain.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

	public User findUserByUsername(String username) {
		String hql = "FROM User u WHERE u.username=:username";
		List<User> list = this.getSession().createQuery(hql).setParameter("username", username).list();
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

}
