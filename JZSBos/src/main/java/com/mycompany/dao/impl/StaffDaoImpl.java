package com.mycompany.dao.impl;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.StaffDao;
import com.mycompany.dao.base.impl.BaseDaoImpl;
import com.mycompany.domain.Staff;

@Repository
public class StaffDaoImpl extends BaseDaoImpl<Staff> implements StaffDao{

}
