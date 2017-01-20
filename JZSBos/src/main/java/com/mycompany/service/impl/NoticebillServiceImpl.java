package com.mycompany.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.dao.NoticebillDao;
import com.mycompany.domain.Noticebill;
import com.mycompany.service.NoticebillService;

@Service
@Transactional
public class NoticebillServiceImpl implements NoticebillService{

	@Autowired
	private NoticebillDao noticebillDao;

	public void save(Noticebill model) {
		noticebillDao.save(model);
	}
	
	
}
