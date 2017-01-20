package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Subarea;
import com.mycompany.utils.PageBean;

public interface SubareaService {

	public void saveOrUpdateBatch(List<Subarea> list);

	public void pageQuery(PageBean pageBean);

	public List<Subarea> findAll();

	public void save(Subarea model);

	public Subarea findById(String id);

	public void update(Subarea subarea);

	public void deleteByIds(String ids);

	public List<Subarea> findListByAssociation(Integer association);

}
