package com.phantom.plane.core.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;

import com.phantom.plane.core.base.BaseException;



public class RsBeanUtil {
	
	/**
	 * 结果集转Map
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	public static List rsToMap(final ResultSet result) throws SQLException{
		List list = new ArrayList(15);
		// 遍历结果集
		while (result.next()) {
			list.add(mapRow(result));
		}
		return list;
	}
	
	
	/**
	 * ResultSet结果集转换成指定Bean对象
	 * @param result
	 * @param clazz
	 * @return
	 */
	public static List rsToBean(final ResultSet result, final Class clazz){
		List list = new ArrayList(15);
		try {
			while (result.next()) {
				Map<String,Object> row = mapRow(result);
				list.add(BeanCreator.create(clazz, row, BeanCreator.DATA_FROM_DB));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (BaseException e) {
			e.printStackTrace();
		} finally {
		}
		return list;
	}
	
	
	/**
	 * 将Map结构类型数据转换成指定Bean对象
	 * @param clazz
	 * @param maps
	 * @return
	 */
	public static List mapToBean(Class clazz,List<Map<String,Object>> maps){
		List list = new ArrayList();
		try {
			for(Map<String, Object> map : maps){
				if(map != null){
					Object bean = BeanCreator.create(clazz, map,BeanCreator.DATA_FROM_DB);
					list.add(bean);
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 将结果集记录
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> mapRow(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> mapOfColValues = new NotNullLinkedCaseInsensitiveMap<Object>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
			// 统一将列表字段转为大写，便于模板展现字段对应
			String key = JdbcUtils.lookupColumnName(rsmd, i).toUpperCase();
			Object val = JdbcUtils.getResultSetValue(rs, i);
			mapOfColValues.put(key, val);
		}
		return mapOfColValues;
	}
	
}