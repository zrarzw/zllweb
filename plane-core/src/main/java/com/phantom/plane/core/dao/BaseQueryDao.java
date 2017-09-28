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
package com.phantom.plane.core.dao;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.FileCopyUtils;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.common.IPagination;
import com.phantom.plane.core.interfacer.IBaseZrarDao;
import com.phantom.plane.core.utils.BaseUtilDao;
import com.phantom.plane.core.utils.BeanCreator;
import com.phantom.plane.core.utils.ClosableResourcePool;
import com.phantom.plane.core.utils.RsBeanUtil;
import com.phantom.plane.core.utils.StringUtil;
import sun.jdbc.rowset.CachedRowSet;

public abstract class BaseQueryDao extends BaseUtilDao implements IBaseZrarDao{

	public BaseQueryDao(){
	}
	
	public BaseQueryDao(HibernateTemplate templateHiberate,
			JdbcTemplate templateJdbc, SqlSession sqlSession) {
		super(templateHiberate, templateJdbc, sqlSession);
	}

	@Override
	public <T> List<T> find(String hql) {
		return templateHiberate.find(hql);
	}

	@Override
	public <T> List<T> find(String hql, Object parameter) {
		return templateHiberate.find(hql, parameter);
	}

	@Override
	public <T> List<T> find(String hql, Object... values) {
		return templateHiberate.find(hql, values);
	}

	@Override
	public Object findUnique(final String hql, final Object... values) {
		return templateHiberate.execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession)
					throws HibernateException, SQLException {
				Query localQuery = paramSession.createQuery(hql);
				if (values != null)
					for (int i = 0; i < values.length; i++)
						localQuery.setParameter(i, values[i]);
				return localQuery.uniqueResult();
			}
		});
	}

	@Override
	public List find(final Class clazz, final Map<String, Object> paramMap) {
		List list = templateHiberate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria criteria = session.createCriteria(clazz);
				for (Iterator<String> it = paramMap.keySet().iterator(); it.hasNext();) {
					String attribute = it.next();
					criteria.add(Restrictions.eq(attribute, paramMap.get(attribute)));
				}
				return criteria.list();
			}
		});
		return list;
	}

	@Override
	public List find(final Class clazz, final String attributeName,final Object value) {
		List list = templateHiberate.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria criteria = session.createCriteria(clazz);
				criteria.add(Restrictions.eq(attributeName, value));
				return criteria.list();
			}
		});
		return list;
	}

	@Override
	public List find(final String hql, final Object[] params, IPagination page) {
		int maxCount = getTotalItems(hql, params).intValue();
		page.setMaxCount(maxCount);
		
		final int start = (page.getPage() - 1) * page.getPageSize();
		final int pageSize = page.getPageSize();
		
		return (List) templateHiberate.execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession)
					throws HibernateException, SQLException {
				Query localQuery = paramSession.createQuery(hql);
				localQuery.setFirstResult(start).setMaxResults(pageSize);
				if (params != null){
					for (int i = 0; i < params.length; i++){
						localQuery.setParameter(i, params[i]);
					}
				}
				return localQuery.list();
			}
		});
	}
	
	@Override
	public List find(final String hql, final Object[] params, final int offset, final int maxResults){
		return (List) templateHiberate.execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession)
					throws HibernateException, SQLException {
				Query localQuery = paramSession.createQuery(hql);
				localQuery.setFirstResult(offset).setMaxResults(maxResults);
				if (params != null){
					for (int i = 0; i < params.length; i++){
						localQuery.setParameter(i, params[i]);
					}
				}
				return localQuery.list();
			}
		});
	}

	@Override
	public <T> T getBO(Class<T> clazz, Serializable id) {
		return templateHiberate.get(clazz, id);
	}

	@Override
	public <T> List<T> getList(String sql, Class<T> clazz) {
		List<Map<String, Object>> maps = templateJdbc.queryForList(sql);
		List list = RsBeanUtil.mapToBean(clazz, maps);
		return list;
	}

	@Override
	public Map<String, Object> getMap(String sql, Object... params) {
		List<Map<String, Object>> mapList = getMapList(sql, params);
		if (mapList != null && mapList.size() > 0)
			return mapList.get(0);
		return null;
	}

	@Override
	public List<Map<String, Object>> getMapList(String sql, Object... params) {
		List<Map<String, Object>> mapList = templateJdbc.queryForList(sql, params);
		return mapList;
	}

	@Override
	public <T> List<T> getList(String sql, Class<T> clazz, List params) {
		int[] argTypes = BeanCreator.getArgTypes(params);
		Object[] args = params.toArray();
		
		List<Map<String, Object>> maps = templateJdbc.queryForList(sql, args, argTypes);
		List list = RsBeanUtil.mapToBean(clazz, maps);
		return list;
	}

	@Override
	public <T> List<T> getList(String sql, Class<T> clazz, Object... params) {
		List<Map<String, Object>> maps = templateJdbc.queryForList(sql, params);
		List list = RsBeanUtil.mapToBean(clazz, maps);
		return list;
	}

	
	@Override
	public String getField(String sql, Object... params) {
		try {
			return getField(sql,String.class,params);
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	
	@Override
	public <T> T getField(String sql, Class<T> clazz, Object... params) {
		try {
			return templateJdbc.queryForObject(sql, clazz,params);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> T getField(String sql, Class<T> clazz) {
		try {
			return templateJdbc.queryForObject(sql, clazz);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public <T> T getField(String sql, Class<T> clazz, List params) {
		try {
			return templateJdbc.queryForObject(sql, clazz, params.toArray());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> getFieldList(String sql, Object... prams) {
		return getFieldList(sql,String.class, prams);
	}

	@Override
	public <T> List<T> getFieldList(String sql, Class<T> clazz,
			Object... params) {
		return templateJdbc.queryForList(sql, clazz,params);
	}

	@Override
	public <T> List<T> getFieldList(String sql, Class<T> clazz, List params) {
		return templateJdbc.queryForList(sql, clazz, params.toArray());
	}

	@Override
	public <T> T getBean(String sql, Class<T> clazz, List params) {
		Map<String, Object> map;
		try {
			int[] argTypes = BeanCreator.getArgTypes(params);
			Object[] args = params.toArray();
			map = templateJdbc.queryForMap(sql, args, argTypes);
		} catch (DataAccessException e1) {
			return null;
		}
		Object bean = null;
		if(null != map){
			try {
				bean = BeanCreator.create(clazz, map,BeanCreator.DATA_FROM_DB);
			} catch (BaseException e) {
				e.printStackTrace();
			}
		}
		return (T)bean;
	}

	@Override
	public <T> T getBean(String sql, Class<T> clazz, Object... params) {
		List list = getList(sql, clazz, params);
		Object bean = null;
		if(list != null && list.size() > 0 ){
			bean = list.get(0);
		}
		return (T) bean;
	}

	@Override
	public int getInt(String sql) {
		
		return templateJdbc.queryForObject(sql, Integer.class);
	}

	@Override
	public int getInt(String sql, Object... params) {
		return templateJdbc.queryForObject(sql, Integer.class,params);
	}

	@Override
	public Long getLong(String sql, Object... params) {
		return templateJdbc.queryForObject(sql,Long.class,params);
	}

	@Override
	public Long getLong(String sql, List params) {
		return templateJdbc.queryForObject(sql,Long.class, params.toArray());
	}

	@Override
	public Long getLong(String sql) {
		return templateJdbc.queryForObject(sql,Long.class);
		
	}

	@Override
	public CachedRowSet getRowSet(String sql) {
		Connection conn = this.getConnection();
		CachedRowSet cachedRowSet = null;
		try {
			cachedRowSet = new CachedRowSet();
			cachedRowSet.setCommand(sql);
			cachedRowSet.execute(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		RowsetPool.add(cachedRowSet);
		ClosableResourcePool.add(cachedRowSet);
		return cachedRowSet;
	}

	@Override
	public CachedRowSet getRowSet(String sql, List params) {
		Connection conn = this.getConnection();
		CachedRowSet cachedRowSet = null;
		try {
			cachedRowSet = new CachedRowSet();
			cachedRowSet.setCommand(sql);
			for (int i = 0; i <params.size(); i++) {
				cachedRowSet.setObject(i + 1, params.get(i));
			}
			cachedRowSet.execute(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		RowsetPool.add(cachedRowSet);
		ClosableResourcePool.add(cachedRowSet);
		return cachedRowSet;
	}

	@Override
	public CachedRowSet getRowSet(String sql, Object... params) {
		Connection conn = this.getConnection();
		CachedRowSet cachedRowSet = null;
		try {
			cachedRowSet = new CachedRowSet();
			cachedRowSet.setCommand(sql);
			if (params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					cachedRowSet.setObject(i + 1, params[i]);
				}
			}
			cachedRowSet.execute(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
//		RowsetPool.add(cachedRowSet);
		ClosableResourcePool.add(cachedRowSet);
		return cachedRowSet;
	}

	@Override
	public ByteArrayOutputStream getBlob(final String sql, final String blobName,
			final Object... params) {
		final DefaultLobHandler localDefaultLobHandler = new DefaultLobHandler();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			templateJdbc.query(sql, params,
					new AbstractLobStreamingResultSetExtractor() {
						public void streamData(ResultSet paramResultSet)
								throws SQLException, IOException {
							// blobName.toLowerCase() : fix intple can't resolve the column
							FileCopyUtils.copy(localDefaultLobHandler.getBlobAsBinaryStream(
									paramResultSet, blobName.toLowerCase()), os);
						}
					});
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return os;
	}

	@Override
	public String getBlobString(String sql, String blobName, String encode,
			Object... params) {
		String result = "";
		ByteArrayOutputStream os = getBlob(sql, blobName, params);
		encode = StringUtil.isNull(encode) ? "UTF-8" : encode;
		try {
			result = new String(os.toByteArray(), encode);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public Long findLong(final String hql, final Object... objects) {
		return (Long) templateHiberate.execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession)
					throws HibernateException, SQLException {
				Query localQuery = paramSession.createQuery(hql);
				int i = 0;
				for (Object obj : objects)
					localQuery.setParameter(i++, obj);
				int s = Integer.valueOf(localQuery.executeUpdate());
				return new Long(((Integer) s).intValue());
			}
		});
	}
	
	
	private Long getTotalItems(String hql, final Object[] params) {
		int i = hql.toUpperCase().indexOf(" ORDER BY ");
		if (i != -1)
			hql = hql.substring(0, i);
		
		QueryTranslatorImpl query = new QueryTranslatorImpl(hql, hql,
				Collections.EMPTY_MAP, (SessionFactoryImplementor) templateHiberate.getSessionFactory());
		query.compile(Collections.EMPTY_MAP, false);
		final String str = "select count(1) from (" + query.getSQLString() + ") tmp_count_t";
		Object result = templateHiberate.execute(new HibernateCallback() {
			public Object doInHibernate(Session paramSession)
					throws HibernateException, SQLException {
				SQLQuery sqlQuery = paramSession.createSQLQuery(str);
				if (params != null)
					for (int i = 0; i < params.length; i++)
						sqlQuery.setParameter(i, params[i]);
				return sqlQuery.uniqueResult();
			}
		});
		return new Long(result.toString());
	}
	
	
	public SqlSession getSqlSession(){
		return this.sqlSession;
	}

	@Override
	public <T> List<T> selectList(String statement) {
		List<T> list = getSqlSession().selectList(statement);
		return list;
	}

	@Override
	public <T> List<T> selectList(String statement, Object parameter) {
		List<T> list = getSqlSession().selectList(statement, parameter);
		return list;
	}
	
	@Override
	public <T> T selectOne(String statement){
		return getSqlSession().selectOne(statement);
	}
	
	public <T> T selectOne(String statement, Object parameter){
		return getSqlSession().selectOne(statement, parameter);
	}
	
	@Override
	public <T> List<T> selectList(String statement, int limit) {
		List<T> list = sqlSession.selectList(statement, new RowBounds(-999, limit));
		return list;
	}

	@Override
	public <T> List<T> selectList(String statement, Object parameter, int limit) {
		List<T> list = sqlSession.selectList(statement, parameter,new RowBounds(-999, limit));
		return list;
	}
	
}
