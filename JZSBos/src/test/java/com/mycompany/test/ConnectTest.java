package com.mycompany.test;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.UserDao;
import com.mycompany.domain.User;
import com.mycompany.utils.MD5Utils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class ConnectTest {

	@Resource
	private UserDao userDao;
	
	@Test
	@Transactional
	@Rollback(false) 
	public void insert() throws Exception{
		String username = "admin";
		String password = "admin";
		password = MD5Utils.md5(password);
		User user = new User();
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		
		user.setId(id);
		user.setUsername(username);
		user.setPassword(password);
		userDao.save(user);
	}
	
		
}
