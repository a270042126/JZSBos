package com.mycompany.web.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hibernate.criterion.DetachedCriteria;

import com.mycompany.utils.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 通用Action实现
 * @param <T>
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2337887015180396853L;
	
	protected PageBean pageBean = new PageBean();
	public PageBean getPageBean() {
		return pageBean;
	}
	
	public DetachedCriteria detachedCriteria = null;
	
	public void setRows(int rows) {
		pageBean.setPageSize(rows);
	}

	public void setPage(int page) {
		pageBean.setCurrentPage(page);
	}
	
	public void setPageNavCount(int pageNavCount){
		pageBean.setPageNavCount(pageNavCount);
	}
	
	
	// 模型对象
	protected T model;
	public T getModel() {
		return model;
	}
	
	/**
	 * 在构造方法中动态获得实现类型，通过反射创建模型对象
	 */
	public BaseAction() {
		ParameterizedType genericSuperclass = null;
		
		if(this.getClass().getGenericSuperclass() instanceof ParameterizedType){
			genericSuperclass = (ParameterizedType) this
			.getClass().getGenericSuperclass();
		}else{//当前为Action创建了代理
			genericSuperclass = (ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass();
		}
		
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		// 获得实体类型
		Class<T> entityClass = (Class<T>) actualTypeArguments[0];
		detachedCriteria = DetachedCriteria.forClass(entityClass);
		pageBean.setDetachedCriteria(detachedCriteria);
		try {
			// 通过反射创建对象
			model = entityClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}





