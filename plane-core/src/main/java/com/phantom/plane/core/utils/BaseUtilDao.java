/**
 * Project:				easyweb-persistence
 * Author:				Green
 * Company: 			杭州中软
 * Created Date:		2014-6-4
 * Description:			请填写该类的功能描述
 * Copyright @ 2014 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.utils;


import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.base.BasePersistenceDao;
import com.phantom.plane.core.exception.BusinessException;
import com.phantom.plane.core.interfacer.IBaseZrarDao;

public abstract class BaseUtilDao extends BasePersistenceDao implements IBaseZrarDao{

	
	public BaseUtilDao(){
	}
	
	public BaseUtilDao(HibernateTemplate templateHiberate,
			JdbcTemplate templateJdbc,SqlSession sqlSession) {
		super(templateHiberate, templateJdbc, sqlSession);
	}

	@Override
	public Blob createBlob(byte[] bytes) throws BaseException {
		try {
			Blob newBlob = Hibernate.createBlob(bytes,
					templateHiberate.getSessionFactory().getCurrentSession());
			return newBlob;
		} catch (HibernateException e) {
			throw new BusinessException(e);
		}
	}
	
	@Override
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(
					templateHiberate.getSessionFactory()).getConnection();
			// 添加进连接池，便于请求结束后释放连接资源
//			ConnectionPool.add(conn);
			ClosableResourcePool.add(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	@Override
	public void evict(Object entity) {
		templateHiberate.evict(entity);
	}
	
	
	@Override
	public String translateSQL(String sql, String id, String sqldic,
			Object... args) {
		return sql;
	}
	
	
	
	
}
