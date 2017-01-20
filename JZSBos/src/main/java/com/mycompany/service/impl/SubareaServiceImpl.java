package com.mycompany.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.SubareaDao;
import com.mycompany.domain.Subarea;
import com.mycompany.service.SubareaService;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class SubareaServiceImpl implements SubareaService{

	@Autowired
	private SubareaDao subareaDao;

	public void saveOrUpdateBatch(List<Subarea> list) {
		for(Subarea subarea: list){
			Subarea existSubarea = subareaDao.findById(subarea.getId());
			if(existSubarea == null){
				subareaDao.save(subarea);
			}else{
				existSubarea.setRegion(subarea.getRegion());
				existSubarea.setAddresskey(subarea.getAddresskey());
				existSubarea.setStartnum(subarea.getStartnum());
				existSubarea.setEndnum(subarea.getEndnum());
				existSubarea.setSingle(subarea.getSingle());
				existSubarea.setPosition(subarea.getPosition());
				subareaDao.update(existSubarea);
			}
		}
	}

	public void pageQuery(PageBean pageBean) {
		subareaDao.pageQuery(pageBean);
	}

	public List<Subarea> findAll() {
		return subareaDao.findAll();
	}

	public void save(Subarea model) {
		subareaDao.save(model);
	}

	public Subarea findById(String id) {
		return subareaDao.findById(id);
	}

	public void update(Subarea subarea) {
		subareaDao.update(subarea);
	}

	public void deleteByIds(String ids) {
		String[] regionIds = ids.split(",");
		for(String id: regionIds){
			subareaDao.executeUpdate("subarea.delete", id);
		}
	}

	/**
	 * 查询没有关联到定区的分区
	 */
	public List<Subarea> findListByAssociation(Integer association) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subarea.class);
		if(association == 0){
			detachedCriteria.add(Restrictions.isNull("decidedzone"));
		}
		return subareaDao.findByCriteria(detachedCriteria);
	}
	
}
