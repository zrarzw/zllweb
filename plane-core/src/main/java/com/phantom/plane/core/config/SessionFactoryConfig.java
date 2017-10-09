/**
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description:工厂配置信息
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年9月22日上午11:11:44
 */
package com.phantom.plane.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import com.github.pagehelper.PageHelper;
import com.phantom.plane.core.datasource.DataSourceRouting;
import com.phantom.plane.core.datasource.DataSourceType;
import com.phantom.plane.core.mybatis.MyBatisBaseDaoImpl;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
public class SessionFactoryConfig {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SessionFactoryConfig.class);
	
	@Value("${spring.datasource.readSize}")
	private String dataSourceSize;

	@Resource(name = "writeDataSource")
	private DataSource writeDataSource;

	@Resource(name = "readDataSource")
	private DataSource readDataSource;
	
	@Value("${spring.jpa.hibernate.dialect}")
	 private String dialect;
	   
	 @Value("${spring.jpa.format-sql}")
	  private String formatSql;
	    
	 @Value("${spring.jpa.show-sql}")
	  private String showSql;
	    
	private SqlSession sqlSession;
	// 多个读库配置
	/*
	 * @Resource(name = "readDataSources") private List<DataSource>
	 * readDataSources;
	 */

	// 直接点属性,说我没初始化,加个get方法就可以,这是在逗我
	private DataSource getWriteDataSource() {
		return writeDataSource;
	}

	private DataSource getReadDataSource() {
		return readDataSource;
	}
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~构建动态dataSource~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
	 * 
	 *	AbstractRoutingDataSource 这破玩意 继承了AbstractDataSource
	 * ,AbstractDataSource又实现了DataSource 所以可以直接丢去构建 SqlSessionFactory
	 */
	@Bean(name = "dataSource")
	public AbstractRoutingDataSource dataSourceProxy() {
		logger.info("springboot---------->多数据源datasource装入动态代理");
		int size = Integer.parseInt(dataSourceSize);
		DataSourceRouting proxy = new DataSourceRouting(size);
		Map<Object, Object> dataSourceMap = new HashMap<>();
		DataSource writeSource = getWriteDataSource();
		DataSource readSource = getReadDataSource();

		dataSourceMap.put(DataSourceType.WRITE.getCode(), writeSource);
		dataSourceMap.put(DataSourceType.READ.getCode(), readSource);
		logger.info("~~~~~~~~~~~~~~~~~~" + DataSourceType.WRITE.getCode());
		// 多个读数据库时
		/*
		 * for (int i = 0; i < size; i++) { dataSourceMap.put(i,
		 * readDataSources.get(i)); }
		 */
		proxy.setDefaultTargetDataSource(writeDataSource);
		proxy.setTargetDataSources(dataSourceMap);
		return proxy;
	}
	/*********************SessionFactory 开始*************************/
		
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~构建mybatis~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
	 * SqlSessionFactory
	 * 
	 * @return SqlSessionFactory
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactorys() {
		logger.info("springboot---------->装载动态sqlSessionFactory");
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceProxy());
		bean.setTypeAliasesPackage("com.phantom");
		// 分页插件
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("reasonable", "true");
		properties.setProperty("supportMethodsArguments", "true");
		properties.setProperty("returnPageInfo", "check");
		properties.setProperty("params", "count=countSql");
		pageHelper.setProperties(properties);
		// 添加插件
		bean.setPlugins(new Interceptor[] { pageHelper });
		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			bean.setMapperLocations(resolver.getResources("classpath*:/mapper/*Mapper.xml"));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

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
	
	/**
	 * ~~~~~~~~~~~~~~~~~构建hibernate sessionFactory~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 * @return SqlSessionFactory
	 */

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		
		logger.info("springboot---------->装载动态hibernate SessionFactory");
		 LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
	        localSessionFactoryBean.setDataSource(dataSourceProxy());
	        Properties properties1 = new Properties();
	        properties1.setProperty("hibernate.dialect", dialect);
	        properties1.setProperty("hibernate.show_sql", showSql);
	        properties1.setProperty("hibernate.format_sql", formatSql);
	        localSessionFactoryBean.setHibernateProperties(properties1);
	        localSessionFactoryBean.setPackagesToScan("com.phantom.plane.*.pojo");
//	        ResourceLoader resourceLoader = new DefaultResourceLoader();
//	        Resource resource = resourceLoader.getResource("resource/");
//	        localSessionFactoryBean.setMappingDirectoryLocations(resource);
	        //设置拦截器
//	        localSessionFactoryBean.setEntityInterceptor(this.auditInterceptor());
	        return localSessionFactoryBean;

	}
	/*********************SessionFactory 结束*************************/	
	/*********************模板 开始*************************/
	/**
	 * ~~~~~~~~~~~~~~~~~构建jdbcTemplate模板~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 
	 * 
	 * @return JdbcTemplate
	 */
	 @Bean(name="jdbcTemplate")
		public JdbcTemplate jdbcTemplate() {
			JdbcTemplate JdbcTemplate = new JdbcTemplate();
			JdbcTemplate.setDataSource(dataSourceProxy());
			return JdbcTemplate;
		}
		/**
		 * ~~~~~~~~~~~~~~~~~构建hbTemplate模板  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 * 
		 * 
		 * @return JdbcTemplate
		 */
	   @Bean(name="hbTemplate")
		public HibernateTemplate hibernateTemplate() {
			HibernateTemplate hibernateTemplate = new HibernateTemplate();
			hibernateTemplate.setSessionFactory(sessionFactory().getObject());
			return hibernateTemplate;
		}
	   /**
		 * ~~~~~~~~~~~~~~~~~构建mybatis模板~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		 * 
		 * 
		 * @return  sqlSession
		 */
	   @Bean(name="sqlSession")
		public SqlSession getSqlSession() {
			if (null == sqlSession) {
				synchronized (MyBatisBaseDaoImpl.class) {
					this.sqlSession = new SqlSessionTemplate(sqlSessionFactorys());
				}
			}
			return this.sqlSession;
		}

}
