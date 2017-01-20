package com.mycompany.dao;

import java.util.List;

import com.mycompany.dao.base.BaseDao;
import com.mycompany.domain.Region;

public interface RegionDao extends BaseDao<Region>{

	public List<Region> findByQ(String q);

}
