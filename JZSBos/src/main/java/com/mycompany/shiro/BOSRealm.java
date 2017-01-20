package com.mycompany.shiro;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.mycompany.dao.FunctionDao;
import com.mycompany.dao.UserDao;
import com.mycompany.domain.Function;
import com.mycompany.domain.User;
import com.mycompany.service.FunctionService;
import com.mycompany.service.UserService;

public class BOSRealm extends AuthorizingRealm{

	@Resource
	private UserService userService;
	@Resource
	private FunctionService functionService;
	
	/**
	 * 授权方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//根据当前登录用户查询其对应的权限，授权
		User user = (User) principals.getPrimaryPrincipal();
		List<Function> list = null;
		if(user.getUsername().equals("admin")){
			//当前用户是超级管理员，查询所有权限，为用户授权
			list = functionService.findAll();
		}else{
			//普通用户，根据用户id查询对应的权限
			list = functionService.findListByUserid(user.getId());
		}
		
		for(Function function:list){
			info.addStringPermission(function.getCode());
		}
		
		return info;
	}

	/**
	 * 认证方法
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证方法。。。");
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();//从令牌获得用户名
		
		User user = userService.findUserByUsername(username);
		if(user == null){
			return null;
		}else{
			//用户名存在
			String password = user.getPassword();
			// 创建简单认证信息对象
						/***
						 * 参数一：签名，程序可以在任意位置获取当前放入的对象
						 * 参数二：从数据库中查询出的密码
						 * 参数三：当前realm的名称
						 */
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password,this.getClass().getSimpleName());
			//返回给安全管理器，由安全管理器负责比对数据库中查询出的密码和页面提交的密码
			return info;
		}
	}

}
