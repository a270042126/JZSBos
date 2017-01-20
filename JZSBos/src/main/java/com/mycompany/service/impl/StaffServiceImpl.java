package com.mycompany.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.StaffDao;
import com.mycompany.domain.Staff;
import com.mycompany.service.StaffService;
import com.mycompany.utils.PageBean;

@Service
@Transactional
public class StaffServiceImpl implements StaffService{

	@Autowired
	private StaffDao staffDao;
	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}

	public void save(Staff model) {
		staffDao.save(model);
	}

	public void pageQuery(PageBean pageBean) {
		staffDao.pageQuery(pageBean);
	}

	public Staff findById(String id) {
		return staffDao.findById(id);
	}

	public void update(Staff staff) {
		staffDao.update(staff);
	}

	public void deleteBatch(String ids) {
		String[] staffIds = ids.split(",");
		for(String id:staffIds){
			staffDao.executeUpdate("staff.delete", id);
		}
	}

	public void restore(String ids) {
		String[] staffIds = ids.split(",");
		for(String id:staffIds){
			staffDao.executeUpdate("staff.restore", id);
		}
	}

	public List<Staff> findListNotDelete() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Staff.class);
		//Restrictions.ne 不等于
		detachedCriteria.add(Restrictions.ne("deltag", "1"));
		return staffDao.findByCriteria(detachedCriteria);
	}
}
