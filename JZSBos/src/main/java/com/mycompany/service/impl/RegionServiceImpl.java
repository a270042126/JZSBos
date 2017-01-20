package com.mycompany.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.RegionDao;
import com.mycompany.domain.Region;
import com.mycompany.service.RegionService;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class RegionServiceImpl implements RegionService{

	@Autowired
	private RegionDao regionDao;

	public void saveBatch(List<Region> list) {
		for(Region region: list){
			regionDao.saveOrUpdate(region);
		}
	}

	public void pageQuery(PageBean pageBean) {
		regionDao.pageQuery(pageBean);
	}

	public void save(Region model) {
		regionDao.save(model);
	}

	public void deleteByIds(String ids) {
		String[] regionIds = ids.split(",");
		for(String id: regionIds){
			regionDao.executeUpdate("region.delete", id);
		}
	}

	public Region findById(String id) {
		return regionDao.findById(id);
	}

	public void update(Region model) {
		regionDao.update(model);
	}

	public List<Region> findAll() {
		return regionDao.findAll();
	}

	public List<Region> findByQ(String q) {
		return regionDao.findByQ(q);
	}
	
	
	
}
