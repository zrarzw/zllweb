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

import java.lang.reflect.Method;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

public class BaseDaoUtil {
	
	
	/**
	 * 获取持久实体类主键配置信息
	 * @param clazz
	 * @return
	 */
	public static Method getIdMethod(Class clazz){
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Id id = method.getAnnotation(Id.class);
			if (null != id) {
				return method;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据查询语句生成记录数统计语句
	 * @param sql
	 * @return
	 */
	public static String generateCountSQL(String sql){
		String temp = sql.toLowerCase();
		// 查询总数时去除order by子句
		int orderIndex = temp.lastIndexOf(" order ");
		if(orderIndex == -1){
			// 刚好换行符\n或tab \t 在order前面
			// sql server 必须去除order by 排序子句
			orderIndex = temp.lastIndexOf("order ");
		}
		if (orderIndex > 0) {
			// 从order by至尾部
			String temp2 = sql.substring(orderIndex);
			int left = StringUtils.countMatches(temp2, "(");
			int right = StringUtils.countMatches(temp2, ")");
			if (left == right) {
				sql = sql.substring(0, orderIndex);
			}
		}
		String countSql = "select count(1) from (" + sql + ") t";
		return countSql;
	}

}
