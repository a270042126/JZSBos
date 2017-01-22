package com.mycompany.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.FunctionDao;
import com.mycompany.dao.base.impl.BaseDaoImpl;
import com.mycompany.domain.Function;

@Repository
public class FunctionDaoImpl extends BaseDaoImpl<Function> implements FunctionDao{

	/**
	 * 根据用户id查询对应的权限
	 */
	public List<Function> findListByUserid(String id) {
		String hql = "SELECT DISTINCT f FROM Function f LEFT OUTER JOIN f.roles r" +
				" LEFT OUTER JOIN r.users u WHERE u.id = :id";
		return this.getSession().createQuery(hql).setParameter("id", id).list();
	}

}
