package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Region;
import com.mycompany.utils.PageBean;

public interface RegionService {

	public void saveBatch(List<Region> list);

	public void pageQuery(PageBean pageBean);

	public void save(Region model);

	public void deleteByIds(String ids);

	public Region findById(String id);

	public void update(Region model);

	public List<Region> findAll();

	public List<Region> findByQ(String q);

}
