package com.mycompany.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.DecidedzoneDao;
import com.mycompany.dao.SubareaDao;
import com.mycompany.domain.Decidedzone;
import com.mycompany.domain.Subarea;
import com.mycompany.service.DecidedzoneService;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class DecidedzoneServiceImpl implements DecidedzoneService{


	@Autowired
	private SubareaDao subareaDao;
	@Autowired
	private DecidedzoneDao decidedzoneDao;

	public void save(Decidedzone model, String[] subareaid) {
		decidedzoneDao.save(model);
		for(String sid: subareaid){
			Subarea subarea = subareaDao.findById(sid);//持久对象
			//分区对象关联定区对象---多方关联一方
			//UPDATE Subarea SET decidedzone = ? WHERE id = ?
			subarea.setDecidedzone(model);
		}
	}

	public void pageQuery(PageBean pageBean) {
		decidedzoneDao.pageQuery(pageBean);
	}

	public Decidedzone findById(String id) {
		return decidedzoneDao.findById(id);
	}

	public void update(Decidedzone decidedzone, String[] subareaid) {
		decidedzoneDao.update(decidedzone);
		
		if(subareaid != null && subareaid.length > 0){
			Set<Subarea> subareas = decidedzone.getSubareas();
			
			//取消绑定旧分区
			for(Subarea subarea: subareas){
				subarea.setDecidedzone(null);
			}
			
			//绑定新分区
			for(String sid: subareaid){
				Subarea subarea = subareaDao.findById(sid);//持久对象
				//分区对象关联定区对象---多方关联一方
				//UPDATE Subarea SET decidedzone = ? WHERE id = ?
				subarea.setDecidedzone(decidedzone);
			}
		}
		
	}

	public void deleteByIds(String ids) {
		String[] decidedzoneIds = ids.split(",");
		for(String id: decidedzoneIds){
			subareaDao.executeUpdate("subarea.deleteDecidedzone",id);
			decidedzoneDao.executeUpdate("decidedzone.delete", id);
		}
	}
	
	
}
