package com.mycompany.web.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.User;
import com.mycompany.service.UserService;
import com.mycompany.utils.MD5Utils;
import com.mycompany.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Results({  
//	@Result(name="index",location = "/common/index.jsp")
	//@Result(name="success", type="redirectAction", params = {"actionName" , "user"})  
})  
public class UserAction extends BaseAction<User>{

	private static final long serialVersionUID = -1257081564706252295L; 
	
	@Resource
	private UserService userService;
	
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}
	
	@Action(value="login",results={@Result(name="home", location="/",type="redirect")})
	public String login(){
		String key = (String) ActionContext.getContext().getSession().get("key");
		////判断用户输入的验证码是否正确
		if(StringUtils.isNotBlank(checkcode) && checkcode.equals(key)){
			//验证码正确
			//获得当前用户对象 
			Subject subject = SecurityUtils.getSubject();
			String password = model.getPassword();
			password = MD5Utils.md5(password);
			AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), password);
			//构造一个用户名密码令牌
			try {
				subject.login(token);
			} catch (UnknownAccountException e) {
				e.printStackTrace();
				this.addActionError(this.getText("usernamenotfound"));
				return "login";
			} catch (Exception e) {
				e.printStackTrace();
				//设置错误信息
				this.addActionError(this.getText("loginError"));
				return "login";
			}
			//获取认证信息对象中存储的User对象
			User user = (User) subject.getPrincipal();
			ActionContext.getContext().getSession().put("loginUser", user);
			return "home";
		}else{
			//验证码错误,设置错误提示信息，跳转到登录页面
			this.addActionError(this.getText("validateCodeError"));
			return "login";
		}
	}
	
	@Action("/editPassword")
	public String editPassword() throws IOException{
		User user = (User) ActionContext.getContext().getSession().get("loginUser");
		//新密码
		String password = model.getPassword();
		password = MD5Utils.md5(password);
		String flag = "1";
		try {
			userService.editPassword(password,user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			//修改密码失败
			flag = "0";
		}
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(flag);
		return NONE;
	}
	
	@Action("/logout")
	public String logout(){
		//销毁session
		ActionContext.getContext().getSession().clear();
		return "login";
	}

}
