package com.mycompany.utils;

import com.mycompany.domain.User;
import com.opensymphony.xwork2.ActionContext;

public class BOSContext {

	public static User getLoginUser(){
		return (User) ActionContext.getContext().getSession().get("loginUser");
	}
}
