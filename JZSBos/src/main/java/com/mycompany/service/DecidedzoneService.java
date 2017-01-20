package com.mycompany.service;

import com.mycompany.domain.Decidedzone;
import com.mycompany.utils.PageBean;

public interface DecidedzoneService {

	public void save(Decidedzone model, String[] subareaid);

	public void pageQuery(PageBean pageBean);

	public Decidedzone findById(String id);

	public void update(Decidedzone decidedzone, String[] subareaid);

	public void deleteByIds(String ids);

}
