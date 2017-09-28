package com.phantom.plane.core.utils;

import java.io.Serializable;

import com.phantom.plane.core.common.IPagination;



/**
 * page object
 * 
 * @author GreenZhao
 * 
 */
public class Pagination implements Serializable,IPagination{

	private static final long serialVersionUID = -1763706226618710464L;
	// current page
	private int page = 1; 
	// page size
	private int pageSize = 50; 
	// max page
	private int maxPage = 1; 
	// next page
	private int nextPage = page + 1; 
	// pre page
	private int prePage = 1; 
	// order field
	private String orderField;
	// order direction
	private String orderDirection;
	// max item count
	private int maxCount = 0;

	private Object pageSet;

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1)
			page = 1;
		nextPage = page >= maxPage ? maxPage : page + 1;
		prePage = page == 1 ? 1 : page - 1;

		this.page = page;

	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		if (maxPage <= 0) {
			this.maxPage = 1;
			page = 1;
		} else {
			if (page > maxPage) {
				setPage(maxPage);
			}
			this.maxPage = maxPage;
		}
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public Object getPageSet() {
		return pageSet;
	}

	public void setPageSet(Object pageSet) {
		this.pageSet = pageSet;
	}

}
