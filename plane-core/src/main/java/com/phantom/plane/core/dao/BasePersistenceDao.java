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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.phantom.plane.core.exception.PersistenceException;
import com.phantom.plane.core.interfacer.IBaseZrarDao;
import com.phantom.plane.core.utils.BaseDaoUtil;

public abstract class BasePersistenceDao implements IBaseZrarDao{
	@Resource
	protected HibernateTemplate templateHiberate;
	@Resource
	protected JdbcTemplate templateJdbc;
	@Resource
	protected SqlSession sqlSession;
	
	public BasePersistenceDao(){
	}
	
	public BasePersistenceDao(HibernateTemplate templateHiberate, 
			JdbcTemplate templateJdbc, SqlSession sqlSession){
		this.templateHiberate = templateHiberate;
		this.templateJdbc = templateJdbc;
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void save(Object bo) throws PersistenceException {
		Class<? extends Object> c = bo.getClass();
		Table t = c.getAnnotation(Table.class);
		
		String schema = t.schema();
		String tableName = null;
		if(schema.length()>0){
			tableName = schema + "." + t.name();
		}else{
			tableName = t.name();
		}
		Method[] methods = c.getDeclaredMethods();
		
		Column column = null;
		List params = new ArrayList();
		StringBuffer columnBuffer = new StringBuffer();
		StringBuffer paramsIndex = new StringBuffer();
		Object value = null;
		for(Method m : methods){
			 column = m.getAnnotation(Column.class);
			 if(null != column){
				 try {
					// 字段值
					value = m.invoke(bo);
					if(value != null){
						// 字段名称
						columnBuffer.append(column.name());
						columnBuffer.append(",");
						// 参数值列表
						params.add(value);
						// 参数占位
						paramsIndex.append("?,");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new PersistenceException(e);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new PersistenceException(e);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw new PersistenceException(e);
				}
			 }
		}
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(tableName);
		sql.append("(");
		sql.append(columnBuffer.substring(0,columnBuffer.length()-1));
		sql.append(")");
		sql.append(" values (");
		sql.append(paramsIndex.substring(0,paramsIndex.length()-1));
		sql.append(")");
		update(sql.toString(), params);
	}
	
	public void saveWith(String statement){
		sqlSession.insert(statement);
	}
	
	public void saveWith(String statement,Object parameter){
		sqlSession.insert(statement, parameter);
	}
	
	@Override
	public void saveBO(Object entity) {
		templateHiberate.save(entity);
		templateHiberate.flush();
	}

	@Override
	public void saveOrUpdateBO(Collection t) {
		templateHiberate.saveOrUpdateAll(t);
		templateHiberate.flush();
	}

	@Override
	public void updateBO(Object t) {
		templateHiberate.saveOrUpdate(t);
		templateHiberate.flush();
	}

	@Override
	public void updateSingleBO(Object t, List<String> columns)
			throws PersistenceException {
		if (null == columns || columns.isEmpty())
			return;
		Table table = t.getClass().getAnnotation(Table.class);
		// 数据库中表名
		String tableName = table.name();
		// 获取主键字段
		Method idMethod = BaseDaoUtil.getIdMethod(t.getClass());
		Column columnAnnotation = idMethod.getAnnotation(Column.class);
		String sId = columnAnnotation.name();

		// 字段名和值存储列表(一一对应)
		List<Object> beanProperties = new ArrayList<Object>();
		List<String> columnList = new ArrayList<String>();

		// 获取主键值
		Object idValue = null;
		try {
			idValue = idMethod.invoke(t);
			for (String column : columns) {
				PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(t,
						column);
				if (null != descriptor) {
					Method method = descriptor.getReadMethod();
					Column col = method.getAnnotation(Column.class);
					columnList.add(col.name());
					beanProperties.add(method.invoke(t));
				}
			}
		} catch (IllegalArgumentException e) {
			throw new PersistenceException(3001, e);
		} catch (IllegalAccessException e) {
			throw new PersistenceException(3001, e);
		} catch (InvocationTargetException e) {
			throw new PersistenceException(3001, e);
		} catch (NoSuchMethodException e) {
			throw new PersistenceException(3001, e);
		}

		// 添加主键值
		beanProperties.add(idValue);

		// 更新语句
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(tableName);

		sql.append(" set ");
		for (String c : columnList) {
			sql.append(c);
			sql.append("=?,");
		}
		String realSql = sql.substring(0, sql.length() - 1);
		realSql += " where " + sId + "=?";
		templateJdbc.update(realSql, beanProperties.toArray());
	}

	@Override
	public void deleteBO(Object entity) {
		templateHiberate.delete(entity);
		templateHiberate.flush();
	}

	@Override
	public void deleteAll(Collection entities) {
		templateHiberate.deleteAll(entities);
		templateHiberate.flush();
	}

	@Override
	public void update(String sql, List params) {
		templateJdbc.update(sql, params.toArray());
	}

	@Override
	public int update(String sql, Object... params) {
		return templateJdbc.update(sql, params);
	}

	@Override
	public int update(String sql) {
		return templateJdbc.update(sql);
	}

	@Override
	public int[] batchUpdate(String sql, final List<List> params) {
		return templateJdbc.batchUpdate(sql, new BatchPreparedStatementSetter(){
			private List<Object> p = null;
			private int size = params.size();
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				p = params.get(i);
				int pSize = p.size();
				Object val = null;
				for (int k = 0; k < pSize; k++) {
					val = p.get(k);
					StatementCreatorUtils.setParameterValue(ps, k+1, 
							SqlTypeValue.TYPE_UNKNOWN, val);
				}
			}
			public int getBatchSize() {
				return size;
			}
		});
	}

	@Override
	public void battchUpdate(String sql, final List<Map<String, Object>> params,
			final List<String> columnNames) {
		templateJdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
			private int size = params.size();
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String, Object> param = params.get(i);
				int columnIndex = 0;
				for (String columnName : columnNames) {
					Object value = param.get(columnName);
					ps.setObject(++columnIndex, value);
				}
			}
			public int getBatchSize() {
				return size;
			}
		});

	}

	@Override
	@Deprecated
	public void updateBatch(String sql, List<List> params) {
		for (List list : params) {
			update(sql, list);
		}
	}

	@Override
	public void updateBatch(String[] sql) {
		templateJdbc.batchUpdate(sql);
	}

	@Override
	public void execute(String sql) {
		templateJdbc.execute(sql);
	}

	@Override
	public void execute(String sql, final Object... args) {
		templateJdbc.execute(sql, new PreparedStatementCallback(){
			public Object doInPreparedStatement(PreparedStatement pstm)
					throws SQLException, DataAccessException {
				for(int i=0;i<args.length;i++){
					pstm.setObject(i+1, args[i]);
				}
				return pstm;
			}
		});
	}
	@Override
	public void execute(String sql, final List args) {
		templateJdbc.execute(sql, new PreparedStatementCallback(){
			public Object doInPreparedStatement(PreparedStatement pstm)
			throws SQLException, DataAccessException {
				for(int i=0;i<args.size();i++){
					pstm.setObject(i+1, args.get(i));
				}
				return pstm;
			}
		});
	}

	@Override
	public void updateBlob(String sql, String blobString, final Object... params) {
		final LobHandler lobHandler;
		try {
			lobHandler = new DefaultLobHandler();
			final byte[] btyesXml = blobString.getBytes("UTF-8");
			templateJdbc.execute(sql,
							new AbstractLobCreatingPreparedStatementCallback(
									lobHandler) {
								protected void setValues(PreparedStatement ps,
										LobCreator lobCreator)
										throws SQLException,
										DataAccessException {
									lobCreator.setBlobAsBytes(ps, 1, btyesXml);
									if(params != null && params.length > 0){
										for(int i = 0 ; i < params.length ;i++){
											ps.setString(i+2, params[i].toString());
										}
									}
								}
							});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	@Override
	public void deleteWith(String statement) {
		sqlSession.delete(statement);
	}

	@Override
	public void deleteWith(String statement, Object parameter) {
		sqlSession.delete(statement, parameter);
	}

	@Override
	public void updateWith(String statement) {
		sqlSession.update(statement);
	}

	@Override
	public void updateWith(String statement, Object parameter) {
		sqlSession.update(statement, parameter);
	}
	
}
