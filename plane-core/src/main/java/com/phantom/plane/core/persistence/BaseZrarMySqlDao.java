/**
 * Project:				easyweb-persistence
 * Author:				Green
 * Company: 			杭州中软
 * Created Date:		2014-10-11
 * Description:			mysql操作接口
 * Copyright @ 2014 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.phantom.plane.core.base.BaseZrarDao;
import com.phantom.plane.core.common.IPagination;
import com.phantom.plane.core.interfacer.IBaseZrarDao;
import com.phantom.plane.core.utils.BaseDaoUtil;
import com.phantom.plane.core.utils.RsBeanUtil;
import com.phantom.plane.core.utils.StringUtil;


public class BaseZrarMySqlDao extends BaseZrarDao implements IBaseZrarDao{
	
	public BaseZrarMySqlDao(){
	}
	
	public BaseZrarMySqlDao(HibernateTemplate templateHiberate,
			JdbcTemplate templateJdbc, SqlSession sqlSession) {
		super(templateHiberate, templateJdbc, sqlSession);
	}
	
	public String generateLimitQuerySql(String sql, int limit) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select * from (");
		sqlBuffer.append(sql);
		sqlBuffer.append(")a limit ");
		sqlBuffer.append(limit);
		return sqlBuffer.toString();
	}
	
	@Override
	public <T> List<T> getList(String sql, Class T, int limit, List params) {
		if (limit <= 0) {
			return new ArrayList<T>();
		}
		String s = generateLimitQuerySql(sql, limit);
		return super.getList(s, T, params);
	}

	@Override
	public String getDatabaseType() {
		return "mysql";
	}

	@Override
	public String generatePageQuerySQL(String sql, IPagination page) {
		StringBuffer queryString = new StringBuffer();
		boolean isContainsOrder = StringUtil.isNotNull(page.getOrderDirection());
		
		if(isContainsOrder){
			queryString.append("select a.* from (");
			queryString.append(sql);
			queryString.append(")a");
			queryString.append(" order by ");
			queryString.append(page.getOrderField());
			queryString.append(" ");
			queryString.append(StringUtil.null2Str(page.getOrderDirection()));
		}else{
			queryString.append(sql);
		}
		queryString.append(" limit #offset#,");
		queryString.append(page.getPageSize());
		return queryString.toString();
	}
	
	
	@Override
	public IPagination pageQuery(String sql, List params, IPagination page, Class clazz) {
		PreparedStatement psQuery = null;
		PreparedStatement psCount = null;
		ResultSet rsQuery = null;
		ResultSet rsCount = null;
		
		Connection conn = getConnection();
		String countSql = BaseDaoUtil.generateCountSQL(sql);
		String queryString = this.generatePageQuerySQL(sql, page);
		
		try {
			int pageSize = page.getPageSize();
			int paramSize = 0;
			// *************************************************
			// 查询记录总数
			psCount = conn.prepareStatement(countSql);
			if (null != params) {
				paramSize = params.size();
				for (int i = 0; i < params.size(); i++) {
					psCount.setObject(i + 1, params.get(i));
				}
			}
			rsCount = psCount.executeQuery();
			while (rsCount.next()) {
				int count = rsCount.getInt(1);
				page.setMaxCount(count);
				int maxPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
				page.setMaxPage(maxPage);
			}
			// *************************************************
			//  查询记录
			int start = (page.getPage() - 1) * pageSize;
			queryString = queryString.replaceAll("#offset#", String.valueOf(start));
			psQuery = conn.prepareStatement(queryString);
			if (null != params) {
				paramSize = params.size();
				for (int i = 0; i < params.size(); i++) {
					psQuery.setObject(i + 1, params.get(i));
					psCount.setObject(i + 1, params.get(i));
				}
			}
			rsQuery = psQuery.executeQuery();
			
			// *************************************************
			// 分页查询结果处理
			List ls = (List) RsBeanUtil.rsToBean(rsQuery, clazz);
			page.setPageSet(ls);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rsQuery != null) {
					rsQuery.close();
				}
				if (rsCount != null) {
					rsCount.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (psQuery != null) {
					psQuery.close();
				}
				if (psCount != null) {
					psCount.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return page;
	}
	
	@Override
	public IPagination pageQuery(String sql, List params,IPagination page){
		PreparedStatement psQuery = null;
		PreparedStatement psCount = null;
		ResultSet rsQuery = null;
		ResultSet rsCount = null;
		
		Connection conn = getConnection();
		String countSql = BaseDaoUtil.generateCountSQL(sql);
		String queryString = this.generatePageQuerySQL(sql, page);
		
		try {
			int pageSize = page.getPageSize();
			int paramSize = 0;
			// *************************************************
			// 查询记录总数
			psCount = conn.prepareStatement(countSql);
			if (null != params) {
				paramSize = params.size();
				for (int i = 0; i < params.size(); i++) {
					psCount.setObject(i + 1, params.get(i));
				}
			}
			rsCount = psCount.executeQuery();
			while (rsCount.next()) {
				int count = rsCount.getInt(1);
				page.setMaxCount(count);
				int maxPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
				page.setMaxPage(maxPage);
			}
			// *************************************************
			//  查询记录
			int start = (page.getPage() - 1) * pageSize;
			queryString = queryString.replaceAll("#offset#", String.valueOf(start));
			psQuery = conn.prepareStatement(queryString);
			if (null != params) {
				paramSize = params.size();
				for (int i = 0; i < params.size(); i++) {
					psQuery.setObject(i + 1, params.get(i));
					psCount.setObject(i + 1, params.get(i));
				}
			}
			rsQuery = psQuery.executeQuery();
			// *************************************************
			// 分页查询结果处理
			List ls = (List) RsBeanUtil.rsToMap(rsQuery);
			page.setPageSet(ls);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rsQuery != null) {
					rsQuery.close();
				}
				if (rsCount != null) {
					rsCount.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (psQuery != null) {
					psQuery.close();
				}
				if (psCount != null) {
					psCount.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return page;
	}
	
	
	public IPagination pageQuery(String sql, List params){
		IPagination page = getPageParams();
		return pageQuery(sql, params, page);
	}

}
