package com.phantom.plane.core.datasource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description:多数据源路由
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年8月27日下午5:09:02
 */
public class DataSourceRouting extends AbstractRoutingDataSource {
	/**
	 * 这里可以做简单负载均衡,暂时用不上
	 */
	private final int dataSourceOrder;
	private AtomicInteger count = new AtomicInteger(0);
	private static Logger logger = Logger.getLogger(DataSourceRouting.class);

	public DataSourceRouting(int _dataSourceOrder) {
		this.dataSourceOrder = _dataSourceOrder;
	}

	/**
	 * 这个方法会根据返回的key去配置文件查找数据源
	 * 
	 * @return
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		logger.info("springboot---------->查找DataSource");
		String targetKey = DataSourceContextHolder.getTargetDataSource();
		/*
		 * if (targetKey.equals(DataSourceType.WRITE.getCode())) return
		 * DataSourceType.WRITE.getCode(); // 读 简单负载均衡 int number =
		 * count.getAndAdd(1); int lookupKey = number % dataSourceOrder; return
		 * new Integer(lookupKey);
		 */
		return targetKey;

	}
}
