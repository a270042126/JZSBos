package com.mycompany.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.dao.FunctionDao;
import com.mycompany.domain.Function;
import com.mycompany.service.FunctionService;

@Service
public class FunctionServiceImpl implements FunctionService{
	
	@Autowired
    private FunctionDao functionDao;

	public List<Function> findAll() {
		return functionDao.findAll();
	}

	public List<Function> findListByUserid(String id) {
		return functionDao.findListByUserid(id);
	}

}
