package com.mycompany.dao;

import java.util.List;

import com.mycompany.dao.base.BaseDao;
import com.mycompany.domain.Function;

public interface FunctionDao extends BaseDao<Function>{

	public List<Function> findListByUserid(String id);

}
