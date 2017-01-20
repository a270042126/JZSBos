package com.mycompany.web.interceptor;

import com.mycompany.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;


public class BOSLoginInterceptor extends MethodFilterInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5893986134987683669L;

	// 拦截方法
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("loginUser");
		if(user == null){
			return "login";
		}else{
			//放行
			return invocation.invoke();
		}
	}
}
