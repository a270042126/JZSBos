package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Role;
import com.mycompany.utils.PageBean;

public interface RoleService {

	public List<Role> findAll();

	public void pageQuery(PageBean pageBean);

	public void save(Role model, String ids);

}
