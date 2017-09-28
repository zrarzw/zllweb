package com.phantom.plane.core.datasource;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description:本地线程全局变量
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年8月27日下午5:05:42
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> dataSourceLocal = new ThreadLocal<>();
	private static Logger logger = Logger.getLogger(DataSourceContextHolder.class);

	public static ThreadLocal<String> getDataSourceLocal() {
		return dataSourceLocal;
	}

	/**
	 * 从库 可以有多个
	 */
	public static void read() {
		dataSourceLocal.set(DataSourceType.READ.getCode());
	}

	/**
	 * 主库 只有一个
	 */
	public static void write() {
		dataSourceLocal.set(DataSourceType.WRITE.getCode());
	}

	public static String getTargetDataSource() {
		return dataSourceLocal.get();
	}

	// 清除数据源
	public static void clearDataSource() {
		dataSourceLocal.remove();
	}

}
