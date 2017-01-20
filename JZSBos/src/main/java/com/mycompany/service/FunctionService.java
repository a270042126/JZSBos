package com.mycompany.service;

import java.util.List;

import com.mycompany.domain.Function;

public interface FunctionService {

	public List<Function> findAll();

	public List<Function> findListByUserid(String id);

}
