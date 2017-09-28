package com.phantom.plane.core.common;

/**
 * 分页对象，用于分页查询和查询结果的返回
 * @author GreenZHAO
 *
 */
public interface IPagination {
	
    /**
     * 
     * 功能：获取默认排序<br>
     * @return
     * @author Thinkpad
     */
	public String getOrderField();
	
	/**
	 * 
	 * 功能：根据页面设置的排序方式进行排序<br>
	 * @param orderField
	 * @author Thinkpad
	 */
	public void setOrderField(String orderField);

	public String getOrderDirection();

	public void setOrderDirection(String orderDirection);

	public int getPage();

	public void setPage(int page);

	public int getPageSize();

	public void setPageSize(int pageSize);

	public int getMaxPage();

	public void setMaxPage(int maxPage);

	public int getNextPage();

	public void setNextPage(int nextPage);

	public int getPrePage();

	public void setPrePage(int prePage);

	public int getMaxCount();

	public void setMaxCount(int maxCount);

	public Object getPageSet();

	public void setPageSet(Object pageSet);
}
