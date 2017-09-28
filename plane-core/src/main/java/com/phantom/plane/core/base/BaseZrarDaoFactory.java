package com.phantom.plane.core.base;


import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.phantom.plane.core.exception.ConfigException;

import com.phantom.plane.core.interfacer.IBaseZrarDao;
import com.phantom.plane.core.persistence.BaseZrarMySqlDao;
import com.phantom.plane.core.utils.ConnectHelper;



/**
 * 数据库资源工厂类
 * 
 * @author GreenZHAO
 * 
 */
public class BaseZrarDaoFactory implements FactoryBean<IBaseZrarDao> {
	
	private HibernateTemplate hbTemplate;
	private JdbcTemplate jdbcTemplate;
	private SqlSession sqlSession;
	private String dbType;

	public BaseZrarDaoFactory(){
	}
	
	public void setHbTemplate(HibernateTemplate hbTemplate) {
		this.hbTemplate = hbTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	
	@Override
	public IBaseZrarDao getObject() throws Exception {
		this.initDbType();
		
		IBaseZrarDao instance = null;
		if("mysql".equals(dbType) ){
			instance = new BaseZrarMySqlDao(hbTemplate, jdbcTemplate, sqlSession);
		}/*else if("dm".equals(dbType)){
			instance = new IBaseDao(hbTemplate, jdbcTemplate, sqlSession);
		}else if("intple".equals(dbType)){
			instance = new IBaseDao(hbTemplate,jdbcTemplate, sqlSession);
		}else if("mssql".equals(dbType)) {
			instance = new IBaseDao(hbTemplate,jdbcTemplate, sqlSession);
		}else if("mysql".equals(dbType)) {
			instance = new IBaseDao(hbTemplate,jdbcTemplate, sqlSession);
		}*/else{
			throw new ConfigException(3002);
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return IBaseZrarDao.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	/**
	 * 初始化数据库类型参数
	 */
	private void initDbType(){
		try {
			String dbType = ConnectHelper.getInstance().getDatabaseType(jdbcTemplate.getDataSource().getConnection());
			this.setDbType(dbType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
