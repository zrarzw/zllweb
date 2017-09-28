/**
 * 
 */
package com.phantom.plane.core.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description: 数据源配置
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年9月22日上午11:05:29
 */
@Configuration
public class DataSourceConfig {

	@Value("${spring.datasource.type}")
	private Class<? extends DataSource> dataSourceType;

	@Value("${spring.datasource.readSize}")
	private String dataSourceSize;

	private static Logger logger = Logger.getLogger(DataSourceConfig.class);

	@Bean(name = "writeDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.write")
	public DataSource writeDataSource() {
		logger.info("springboot---------->装载writeDataSource");
		return DataSourceBuilder.create().type(dataSourceType).build();

	}

	/**
	 * 有多少个从库就要配置多少个
	 * 
	 * @return
	 */
	@Bean(name = "readDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.read")
	public DataSource readDataSourceOne() {
		logger.info("springboot---------->装载readDataSource");
		return DataSourceBuilder.create().type(dataSourceType).build();
	}

	/**
	 * 有多少个读库配置
	 * 
	 * @return
	 */
	/*
	 * @Bean("readDataSources") public List<DataSource> readDataSources(){
	 * List<DataSource> dataSources=new ArrayList<>();
	 * dataSources.add(readDataSourceOne());
	 * dataSources.add(readDataSourceTwo()); return dataSources; }
	 */
}
