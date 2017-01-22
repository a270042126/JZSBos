package com.mycompany.dao.impl;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.RoleDao;
import com.mycompany.dao.base.impl.BaseDaoImpl;
import com.mycompany.domain.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao{

}
