package com.mycompany.dao.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

import com.mycompany.dao.base.BaseDao;
import com.mycompany.utils.PageBean;


public class BaseDaoImpl<T> implements BaseDao<T> {

	// 实体类型
	private Class<T> entityClass;
	// 使用注解方式进行依赖注入
	private SessionFactory sessionFactory; 
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {  
        this.sessionFactory = sessionFactory;  
    }  
	
	 public Session getSession() {  
	        return sessionFactory.getCurrentSession();  
	   }  
	
	public BaseDaoImpl() {
		// 获得父类（BaseDaoImpl<T>）类型
		ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 获得父类上的泛型数组
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		entityClass = (Class<T>) actualTypeArguments[0];
	}
		
	public void save(T entity) {
		this.getSession().save(entity);
	}

	public void delete(T entity) {
		this.getSession().delete(entity);
	}

	public void update(T entity) {
		this.getSession().update(entity);
	}

	public void saveOrUpdate(T entity) {
		this.getSession().saveOrUpdate(entity);
	}

	public T findById(Serializable id) {
		return (T) this.getSession().get(entityClass, id);
	}

	public List<T> findAll() {// FROM User
		String hql = "FROM  " + entityClass.getSimpleName();
		return (List<T>) this.getSession().createQuery(hql).list();
	}

	/**
	 * 通用更新方法
	 */
	public void executeUpdate(String queryName, Object... objects) {
		Session session = this.getSession();
		// 使用命名查询语句获得一个查询对象
		Query query = session.getNamedQuery(queryName);
		//Query query = session.createQuery(queryName);
		if (objects != null && objects.length > 0) {  
            for (int i = 0; i < objects.length; i++) {  
            	query.setParameter("" + i, objects[i]);  
            }  
        }  
		query.executeUpdate();// 执行更新
	}
	
	

	/**
	 * 通用分页查询方法
	 */
	public void pageQuery(PageBean pageBean) {
		int currentPage = pageBean.getCurrentPage();
		int pageSize = pageBean.getPageSize();
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		//总数据量----select count(*) from bc_staff
		//select count(*) from bc_staff
	    //改变Hibernate框架发出的sql形式
		detachedCriteria.setProjection(Projections.rowCount());
		
		Integer total = Integer.valueOf(detachedCriteria.getExecutableCriteria(this.getSession()).uniqueResult().toString());
		pageBean.setTotal(total);//设置总数据量
		pageBean.setTotalPage( ((total + pageSize) - 1) / pageSize);//设置总页数
		int totalPage = pageBean.getTotalPage();
		pageBean.setHasPrePage(currentPage == 1 ? false : true); //设置上一个页面按钮是否可用
		pageBean.setHasNextPage(currentPage == totalPage || totalPage == 0 ? false : true); //设置下一个页面按钮是否可用
		
		//设置页面的起点页和结束页
		int pageNavCount = pageBean.getPageNavCount();
		int temp = pageNavCount / 2 == 0 ? pageNavCount / 2 : pageNavCount / 2 + 1;
		if(pageNavCount >= totalPage){
			pageBean.setNavBeginPage(1);
			pageBean.setNavEndPage(totalPage);
		}else if(temp > totalPage - currentPage){
			pageBean.setNavBeginPage(totalPage - pageNavCount + 1);
			pageBean.setNavEndPage(totalPage);
		}else if(temp > currentPage){
			pageBean.setNavBeginPage(1);
			pageBean.setNavEndPage(pageNavCount);
		}else{
			pageBean.setNavBeginPage(currentPage - temp + 1);
			pageBean.setNavEndPage(currentPage + temp - 1);
		}
		
		detachedCriteria.setProjection(null);//修改sql的形式为select * from ....
		//重置表和类的映射关系
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		//当前页展示的数据集合
		int firstResult = (currentPage - 1) * pageSize;
		int maxResults = pageSize;
		
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(this.getSession());
		//criteria.setProjection(null);
		//criteria.setResultTransformer(Criteria.ROOT_ENTITY); 
		criteria.setFirstResult(firstResult); 
		criteria.setMaxResults(maxResults);
		
		List rows = criteria.list();
		pageBean.setRows(rows);
	}
	
	public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(this.getSession());
		return criteria.list();
	}

}
