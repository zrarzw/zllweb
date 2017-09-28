/**
 * 
 */
package com.phantom.plane.core.datasource;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description: 自定义事务
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年8月27日下午5:45:54
 */
@Configuration
@EnableTransactionManagement
public class DataSourceTransactionManager extends DataSourceTransactionManagerAutoConfiguration {
	/**
	 * 自定义事务 MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.
	 * SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，
	 * 否则事务管理会不起作用。
	 * 
	 * @return
	 */
	@Resource(name = "writeDataSource")
	private DataSource dataSource;

	@Bean(name = "transactionManager")
	public org.springframework.jdbc.datasource.DataSourceTransactionManager transactionManagers() {

		return new org.springframework.jdbc.datasource.DataSourceTransactionManager(dataSource);
	}
}
