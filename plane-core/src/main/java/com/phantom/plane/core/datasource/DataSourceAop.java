/**
* 
*/
package com.phantom.plane.core.datasource;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;



/**
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description: 数据源切换AOP方式
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年8月27日下午5:56:42
 */
@Aspect
@Component
public class DataSourceAop {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(DataSourceAop.class);

	@Before("execution(* com.phantom.plane.*.daoImp..*.select*(..)) || execution(* com.phantom.plane.*.daoImp..*.get*(..))")
	public void setReadDataSourceType() {
		DataSourceContextHolder.read();
		logger.info("springboot---------->进入read数据源");

	}

	@Before("execution(* com.phantom.plane.*.daoImp..*.insert*(..)) || execution(* com.phantom.plane.*.daoImp..*.update*(..))")
	public void setWriteDataSourceType() {
		DataSourceContextHolder.write();
		logger.info("springboot---------->进入write数据源");

	}
}
