package com.mycompany.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.RegionDao;
import com.mycompany.dao.base.impl.BaseDaoImpl;
import com.mycompany.domain.Region;

@Repository
public class RegionDaoImpl extends BaseDaoImpl<Region> implements RegionDao{

	public List<Region> findByQ(String q) {
		String hql = "FROM Region WHERE province LIKE ?0 OR city LIKE ?1 OR district LIKE ?2";
		return this.getSession().createQuery(hql).setParameter("0", "%"+q+"%")
				.setParameter("1", "%"+q+"%").setParameter("2", "%"+q+"%").list();
	}

}
