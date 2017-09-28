package com.phantom.plane.core.config;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * MyBatis配置
 */
@Configuration
// TODO 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
@AutoConfigureAfter(SessionFactoryConfig.class)
public class MyBatisMapperScannerConfig {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MyBatisMapperScannerConfig.class);

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		logger.info("springboot---------->装载mybatis的mapper");
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		// 这里的BasePackage 不能写com.training,估计跟spring 的扫描冲突,会实例化两个service,应该需要重构目录
		mapperScannerConfigurer.setBasePackage("com.phantom.plane.*.dao。*");
		Properties properties = new Properties();
		properties.setProperty("mappers", Mapper.class.getName());
		properties.setProperty("notEmpty", "false");
		properties.setProperty("IDENTITY", "MYSQL");
		mapperScannerConfigurer.setProperties(properties);
		return mapperScannerConfigurer;
	}
}