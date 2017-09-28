/*
 * Project:			easyweb-persistence
 * Author:			赵志武
 * Company: 		杭州中软
 * Created Date:	2011-10-10
 * Copyright @ 2011 CS&S.COM - Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description		
 * */

package com.phantom.plane.core.interfacer;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.common.IPagination;
import com.phantom.plane.core.exception.PersistenceException;

import sun.jdbc.rowset.CachedRowSet;



/**
 * 数据库操作对象接口
 * 
 * @author GreenZhao
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface IBaseZrarDao extends Serializable{

	
	
	/**
	 * 通过原生方式保存BO对象（不使用hibernate）
	 * 
	 * @param t
	 *            BO对象
	 * @throws PersistenceException
	 */
	public void save(Object t) throws PersistenceException;
	
	
	
	/**
	 * 通过映射配置ID执行插入语句
	 * @param statement
	 * @since 3.0.4
	 */
	public void saveWith(String statement);
	
	
	/**
	 * 通过映射配置ID执行插入语句
	 * @param statement
	 * @param parameter
	 * @since 3.0.4
	 */
	public void saveWith(String statement,Object parameter);
	
	
	/**
	 * 保存对象
	 * 
	 * @param t
	 *            BO对象
	 */
	public abstract void saveBO(Object t);

	/**
	 * 批量保存或者更新对象实例
	 * 
	 * @param t
	 *            对象实例集合
	 */
	public abstract void saveOrUpdateBO(Collection t);

	/**
	 * 对象更新
	 * 
	 * @param t
	 *            BO对象
	 */
	public abstract void updateBO(Object t);

	/**
	 * 更新对象指定属性值,未指定的属性不予以更新
	 * 
	 * @param t
	 *            将要进行更新的对象
	 * @param columns
	 *            要更新的字段
	 * @throws PersistenceException
	 */
	public abstract void updateSingleBO(Object t, List<String> columns)
			throws PersistenceException;

	/**
	 * 对象删除
	 * 
	 * @param t
	 *            BO对象
	 * 
	 */
	public abstract void deleteBO(Object t);
	
	
	/**
	 * 删除集合
	 * 
	 * @param entities
	 *            集合对象
	 */
	public abstract void deleteAll(Collection entities);
	
	
	/**
	 * 根据映射配置ID执行删除操作
	 * @param statement 配置ID
	 * @since 3.0.4
	 */
	public void deleteWith(String statement);
	
	/**
	 * 根据映射配置ID执行删除操作
	 * @param statement 配置ID
	 * @param parameter	参数对象
	 * @since 3.0.4
	 */
	public void deleteWith(String statement,Object parameter);
	

	/**
	 * HQL语句查询，返回结果为List类型
	 * 
	 * @param hql
	 *            HQL查询语句
	 * @return java.util.List
	 */
	public abstract <T> List<T> find(String hql);

	/**
	 * HQL语句查询，返回结果为List类型
	 * 
	 * @param hql
	 *            HQL查询语句
	 * @param parameter
	 *            查询参数
	 * @return java.util.List
	 */
	public abstract <T> List<T> find(String hql, Object parameter);

	/**
	 * HQL语句查询，返回结果为List类型
	 * 
	 * @param hql
	 *            HQL查询语句
	 * @param values
	 *            可选参数
	 * @return java.util.List
	 */
	public abstract <T> List<T> find(String hql, Object... values);

	/**
	 * HQL语句查询，返回结果为单一对象
	 * 
	 * @param hql
	 *            HQL查询语句
	 * @param values
	 *            可选参数
	 * @return Object
	 */
	public abstract Object findUnique(String hql, Object... values);

	/**
	 * HQL语句查询,返回结果为List类型
	 * 
	 * @param clazz
	 *            Class类型
	 * @param paramMap
	 *            参数列表
	 * @return java.util.List
	 */
	public abstract List find(final Class clazz,
			final Map<String, Object> paramMap);

	/**
	 * HQL语句查询，返回结果为List类型
	 * 
	 * @param clazz
	 *            Class类型
	 * @param attributeName
	 *            属性
	 * @param value
	 *            属性值
	 * @return java.util.List 符合查询条件的结果集
	 */
	public abstract List find(final Class clazz, final String attributeName,
			final Object value);

	/**
	 * HQL语句查询，返回结果为List类型
	 * 
	 * @param hql
	 *            hql语句
	 * @param params
	 *            参数列表
	 * @param page
	 *            分页对象
	 * @return java.util.List 符合查询条件的结果集
	 */
	public abstract List find(String hql, Object[] params, IPagination page);


	/**
	 * HQL语句查询
	 * @param hql		HQL语句
	 * @param params	参数列表
	 * @param offset	起始位置
	 * @param maxResult	 数据行数
	 * @return
	 */
	public List find(final String hql, final Object[] params, int offset, int maxResult);

	
	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @return IPagination 包含查询结果的分页对象
	 */
	public abstract IPagination pageQuery(String sql, List params);

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            查询sql
	 * @param params
	 *            查询参数
	 * @param page
	 *            初始化的分页对象
	 * @return IPagination 包含查询结果的分页对象
	 */
	public abstract IPagination pageQuery(String sql, List params,
			IPagination page);

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param params
	 *            参数列表
	 * @param clazz
	 *            记录类型
	 * @return IPagination 包含查询结果的分页对象
	 */
	public abstract IPagination pageQuery(String sql, List params, Class clazz);

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param params
	 *            参数列表
	 * @param page
	 *            分页对象
	 * @param clazz
	 *            记录类型
	 * @return Pagination 分页对象
	 */
	public abstract IPagination pageQuery(String sql, List params,
			IPagination page, Class clazz);


	
	/**
	 * 使用映射配置进行分页查询
	 * @param statement
	 * @param parameter
	 * @return
	 * @since 3.0.4
	 */
	public abstract IPagination pageQueryWith(String statement,Object parameter);
	
	
	/**
	 * 使用映射配置进行分页查询
	 * @param statement
	 * @param parameter
	 * @param page
	 * @return
	 * @since 3.0.4
	 */
	public abstract IPagination pageQueryWith(String statement,Object parameter, IPagination page);
	
	
	/**
	 * 查找对象
	 * 
	 * @param clazz
	 *            BO对象
	 * @param id
	 *            主键
	 * @return java.lang.Object
	 */
	public abstract <T> T getBO(Class<T> clazz, Serializable id);
	
	
	/**
	 * 获取数据库连接
	 * 
	 * @return java.sql.Connection
	 */
	public abstract Connection getConnection();
	
	
	/**
	 * SQL语句查询，返回数据为List类型
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            返回结果中实体类型class
	 * @return List 符合查询结果的clazz实体类集合
	 */
	public abstract <T> List<T> getList(String sql, Class<T> clazz);

	/**
	 * SQL语句查询，返回数据为Map类（适用于一行语句的查询）型
	 * 
	 * @param sql
	 *            查询sql
	 * @param params
	 *            参数值
	 * @return Map<String,Object> map形式的一行数据
	 */
	public abstract Map<String, Object> getMap(String sql, Object... params);

	/**
	 * SQL语句查询，返回数据为List<Map<String,Object>类（适用于多行语句的查询）型
	 * 
	 * @param sql
	 *            查询sqll
	 * @param params
	 *            参数值
	 * @return List<Map<String,Object>> 符合查询条件的结果集（一行数据对应一个map，key为列名，value为列值）
	 */
	public abstract List<Map<String, Object>> getMapList(String sql,
			Object... params);

	/**
	 * SQL语句查询，返回数据为List类型
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            返回结果中实体类型class
	 * @param params
	 *            参数列表
	 * @return List<T> 符合查询条件的结果集
	 */
	public abstract <T> List<T> getList(String sql, Class<T> clazz, List params);

	/**
	 * SQL语句查询，返回数据为List类型
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            返回结果中实体类型class
	 * @param param
	 *            参数列表
	 * @return List<T> 符合查询条件的结果集
	 */
	public abstract <T> List<T> getList(String sql, Class<T> clazz,
			Object... param);

	/**
	 * 返回指定行数的查询结果集
	 * 
	 * @param <T>
	 * @param sql
	 * @param T
	 * @param limit
	 * @return
	 * @since 3.0.4
	 */
	<T> List<T> getList(String sql, Class T, int limit, List params);

	/**
	 * 查询数据库表中单某条记录某一字段值(默认String类型)<br>
	 * 该类查询为唯一性查询，返回结果通常为一条记录
	 * 
	 * @param sql
	 *            查询sql
	 * @return 查询结果
	 */
	public abstract String getField(String sql, Object... params);

	/**
	 * 查询数据库表中单某条记录某一字段值(默认String类型)<br>
	 * 该类查询为唯一性查询，返回结果通常为一条记录
	 * 
	 * @param sql
	 *            查询sql
	 * @return T 查询结果
	 */
	public abstract <T> T getField(String sql, Class<T> clazz, Object... params);

	/**
	 * 查询数据库表中单某条记录某一字段值<br>
	 * 该类查询为唯一性查询，返回结果通常为一条记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            字段值类型(如：String.claas或Long.class等)
	 * @return T 查询结果
	 */
	public abstract <T> T getField(String sql, Class<T> clazz);

	/**
	 * 查询数据库表中单某条记录某一字段值<br>
	 * 该类查询为唯一性查询，返回结果通常为一条记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            字段值类型(如：String.claas或Long.class等)
	 * @param params
	 *            参数列表
	 * @return T 查询结果
	 */
	public abstract <T> T getField(String sql, Class<T> clazz, List params);

	/**
	 * 查询数据库表中某一列值，结果以List<String>返回
	 * 
	 * @param sql
	 *            查询sql
	 * @param prams
	 *            查询参数
	 * @return List<String> 查询结果
	 */
	public abstract List<String> getFieldList(String sql, Object... prams);

	/**
	 * 查询数据库表中某一列值，结果以List返回
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            字段值类型(如：String.claas或Long.class等)
	 * @param params
	 *            查询参数
	 * @return List<T> 查询结果
	 */
	public abstract <T> List<T> getFieldList(String sql, Class<T> clazz,
			Object... params);

	/**
	 * 查询数据库表中某一列值，结果以List返回
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            字段值类型(如：String.claas或Long.class等)
	 * @param params
	 *            参数列表
	 * @return List<T> 查询结果
	 */
	public abstract <T> List<T> getFieldList(String sql, Class<T> clazz,
			List params);

	/**
	 * SQL语句查询，结果转换为具体对象(BO或VO)<br>
	 * 该类查询多为唯一性查询，查询结果通常为一条记录或者无记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            BO或VO Class
	 * @param params
	 *            参数列表
	 * @return T 查询结果
	 */
	public <T> T getBean(String sql, Class<T> clazz, List params);

	/**
	 * SQL语句查询，结果转换为具体对象(BO或VO)<br>
	 * 该类查询多为唯一性查询，查询结果通常为一条记录或者无记录
	 * 
	 * @param sql
	 *            SQL语句
	 * @param clazz
	 *            BO或VO Class
	 * @param params
	 *            参数列表
	 * @return T 查询结果
	 */
	public <T> T getBean(String sql, Class<T> clazz, Object... params);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 */
	public abstract void update(String sql, List params);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 */
	public abstract int update(String sql, Object... params);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            SQL语句
	 * 
	 */
	public abstract int update(String sql);

	/**
	 * 批量更新
	 * 
	 * @param sql
	 *            sql更新语句
	 * @param params
	 *            列值列表（一个list对应一组列值，添加顺序要和sql语句的参数列顺序保持一致）
	 */
	public abstract int[] batchUpdate(String sql, List<List> params);

	/**
	 * 批量更新
	 * 
	 * @param sql
	 *            执行的sql更新语句
	 * @param params
	 *            列值列表（一个Map包含一组列值，key为sql语句中的列名，value为列名对应的列值）
	 * @param columnNames
	 *            列名列表（注意：列名的添加顺序要与sql语句中列名出现的顺序保持一致）
	 */
	public abstract void battchUpdate(String sql,
			List<Map<String, Object>> params, List<String> columnNames);

	/**
	 * 查询结果为int类型的查询，多数为<code>select count(*) from A 或计算型SQL语句</code>
	 * 
	 * @param sql
	 *            SQL语句
	 * @return int 查询结果
	 */
	public abstract int getInt(String sql);

	/**
	 * 查询结果为int类型的查询，多数为<code>select count(*) from A 或计算型SQL语句</code>
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 * @return int 查询结果
	 */
	public abstract int getInt(String sql, Object... params);

	/**
	 * 查询结果为Long类型的查询，多数为<code>select count(*) from A 或计算型SQL语句</code>
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 * @return Long 查询结果
	 */
	Long getLong(String sql, Object... params);

	/**
	 * 查询结果为Long类型的查询，多数为<code>select count(*) from A 或计算型SQL语句</code>
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 * @return Long 查询结果
	 */
	public abstract Long getLong(String sql, List params);

	/**
	 * 查询结果为Long类型的查询，多数为<code>select count(*) from A 或计算型SQL语句</code>
	 * 
	 * @param sql
	 *            SQL语句
	 * @return Long 查询结果
	 */
	public abstract Long getLong(String sql);

	/**
	 * 批量插入
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            记录集
	 */
	public abstract void updateBatch(String sql, List<List> params);

	/**
	 * 批量更新
	 * 
	 * @param sql
	 *            SQL语句数组
	 */
	public abstract void updateBatch(String[] sql);

	/**
	 * 查询结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @return CachedRowSet 结果集
	 */
	public abstract CachedRowSet getRowSet(String sql);

	/**
	 * 查询结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 * @return CachedRowSet 结果集
	 */
	public abstract CachedRowSet getRowSet(String sql, List params);

	/**
	 * 查询结果集
	 * 
	 * @param sql
	 *            SQL语句
	 * @param params
	 *            参数列表
	 * @return CachedRowSet 结果集
	 */
	public abstract CachedRowSet getRowSet(String sql, Object... params);

	/**
	 * 查询BLOB字段流
	 * 
	 * @param sql
	 *            SQL语句
	 * @param blobName
	 *            BLOB字段名称
	 * @param params
	 *            参数列表
	 * @return ByteArrayOutputStream 字节数组流
	 */
	public abstract ByteArrayOutputStream getBlob(String sql, String blobName,
			Object... params);

	/**
	 * 查询BLOB字段字符串
	 * 
	 * @param sql
	 *            SQL语句
	 * @param blobName
	 *            BLOB字段名称
	 * @param params
	 *            参数列表
	 * @param encode
	 *            字符串编码：默认为UTF-8
	 * @return String 字符串形式的blob字段
	 */
	public abstract String getBlobString(String sql, String blobName,
			String encode, Object... params);

	/**
	 * 执行指定SQL语句
	 * 
	 * @param sql
	 *            查询sql
	 */
	public abstract void execute(String sql);

	/**
	 * 指定指定SQL语句
	 * 
	 * @param sql
	 * @param args
	 */
	public void execute(String sql, final Object... args);
	
	/**
	 * 指定指定SQL语句
	 * 
	 * @param sql
	 * @param args
	 */
	public void execute(String sql, final List args);

	/**
	 * 查询结果为Long类型的查询
	 * 
	 * @param hql
	 *            HQL语句
	 * @param objects
	 *            参数列表
	 * @return Long 查询结果
	 */
	public abstract Long findLong(String hql, Object... objects);

	/**
	 * 分页查询
	 * 
	 * @param hql
	 * @param params
	 *            参数数组
	 * @param page
	 *            分页对象
	 * @return IPagination 包含查询结果集的分页对象
	 */
	public abstract IPagination pageQuery(String hql, Object[] params,
			IPagination page);

	/**
	 * 将对象从Session缓存清除
	 * 
	 * @param object
	 */
	public abstract void evict(Object object);

	/**
	 * 更新BLOB字段 第一个?必须为要更改的BLOB字段
	 * 
	 * @param sql
	 *            更新sql
	 * @param blobString
	 *            需要更新的字符串形式的blob字段值
	 * @param params
	 *            查询参数
	 */
	void updateBlob(String sql, String blobString, Object... params);

	
	/**
	 * 根据映射配置ID执行更新操作
	 * @param statement 配置ID
	 * @since 3.0.4
	 */
	public void updateWith(String statement);
	
	
	/**
	 * 根据映射配置ID执行更新操作
	 * @param statement 配置ID
	 * @param parameter	参数对象
	 * @since 3.0.4
	 */
	public void updateWith(String statement,Object parameter);
	
	
	
	
	/**
	 * 创建Blob对象
	 * 
	 * @param bytes
	 * @return Blob 新创建的blob对象
	 * @throws BaseException
	 */
	Blob createBlob(byte[] bytes) throws BaseException;

	/**
	 * 不同数据库适配接口，默认实现不做适配
	 * 
	 * @param sql
	 *            原sql语句
	 * @param id
	 *            目标sql语句标识
	 * @param sqldic
	 *            目标sql字典
	 * @param args
	 *            可选参数
	 * @return 适配后的sql语句
	 */
	public String translateSQL(String sql, String id, String sqldic,
			Object... args);
	
	
	/**
	 * 根据映射配置ID查询结果集
	 * @param <T>
	 * @param statement	映射配置ID
	 * @return
	 * @since 3.0.4
	 */
	public <T> List<T> selectList(String statement);

	
	/**
	 * 根据映射配置ID查询结果集
	 * @param <T>
	 * @param statement 映射配置ID
	 * @param parameter	参数对象
	 * @return
	 * @since 3.0.4
	 */
	public <T> List<T> selectList(String statement, Object parameter);
	
	
	/**
	 * 根据映射配置ID查询指定数目的结果集
	 * @param <T>
	 * @param statement
	 * @param limit
	 * @return
	 */
	public <T> List<T> selectList(String statement, int limit);
	
	
	/**
	 * 根据映射配置ID查询指定数目的结果集
	 * @param <T>
	 * @param statement
	 * @param parameter
	 * @param limit
	 * @return
	 */
	public <T> List<T> selectList(String statement, Object parameter, int limit);
	
	
	/**
	 * 根据映射配置ID查询单条记录
	 * @param <T>
	 * @param statement 映射配置ID
	 * @return
	 * @since 3.0.4
	 */
	public <T> T selectOne(String statement);
	
	
	/**
	 * 根据映射配置ID查询单条记录
	 * @param <T>
	 * @param statement	映射配置ID
	 * @param parameter	参数对象
	 * @return
	 * @since 3.0.4
	 */
	public <T> T selectOne(String statement, Object parameter);
	
	
	
	/**
	 * 获取数据库类型
	 * 
	 * @return 数据库类型字符串
	 * @since 3.0.4
	 */
	public String getDatabaseType();
	

}