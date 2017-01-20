package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Staff;
import com.mycompany.utils.PageBean;

public interface StaffService {

	public void save(Staff model);

	public void pageQuery(PageBean pageBean);

	public Staff findById(String id);

	public void update(Staff staff);

	public void deleteBatch(String ids);

	public void restore(String ids);

	public List<Staff> findListNotDelete();

}
