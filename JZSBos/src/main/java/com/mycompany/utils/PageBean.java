package com.mycompany.utils;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * 封装分页信息
 * @author dg
 *
 */
public class PageBean {
	private int currentPage;//当前页码
	private int pageSize;//每页显示记录数
	private DetachedCriteria detachedCriteria;//离线条件查询对象，包装查询条件
	private int total;//总记录数
	private List rows;//当前页需要展示的数据集合
    private int totalPage;  // 总页数(totalPage)  
    private boolean hasPrePage;  // 是否有上一页(hasPrePage)
    private boolean hasNextPage; // 是否有下一页(hasNextPage)  
    private int pageNavCount;//显示页数
    private int navBeginPage;//可见起始页(beginIndex) 
    private int navEndPage;//可见结束页(beginIndex) 
	
	
	public int getNavEndPage() {
		return navEndPage;
	}
	public void setNavEndPage(int navEndPage) {
		this.navEndPage = navEndPage;
	}
	public int getNavBeginPage() {
		return navBeginPage;
	}
	public void setNavBeginPage(int navBeginPage) {
		this.navBeginPage = navBeginPage;
	}
	public int getPageNavCount() {
		if(pageNavCount > 0)
			return pageNavCount;
		else
			return 9;
	}
	public void setPageNavCount(int pageNavCount) {
		this.pageNavCount = pageNavCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public boolean isHasPrePage() {
		return hasPrePage;
	}
	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public int getCurrentPage() {
		if(currentPage > 0)
			return currentPage;
		else
			return 1;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		if(pageSize  > 0)
			return pageSize;
		return 40;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}
	public void setDetachedCriteria(DetachedCriteria detachedCriteria) {
		this.detachedCriteria = detachedCriteria;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	
}
