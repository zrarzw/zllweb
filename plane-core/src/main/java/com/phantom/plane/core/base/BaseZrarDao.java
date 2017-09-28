package com.phantom.plane.core.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.phantom.plane.core.common.IPagination;
import com.phantom.plane.core.common.SysConstant;
import com.phantom.plane.core.dao.BaseQueryDao;
import com.phantom.plane.core.interfacer.IBaseZrarDao;
import com.phantom.plane.core.utils.BaseDaoUtil;
import com.phantom.plane.core.utils.Pagination;
import com.phantom.plane.core.utils.RsBeanUtil;
import com.phantom.plane.core.utils.StringUtil;
import com.phantom.plane.core.utils.ThreadPool;


public abstract class BaseZrarDao extends BaseQueryDao implements IBaseZrarDao{
	
	public BaseZrarDao(){
	}
	
	public BaseZrarDao(HibernateTemplate templateHiberate,
			JdbcTemplate templateJdbc, SqlSession sqlSession) {
		super(templateHiberate, templateJdbc, sqlSession);
	}

	public IPagination pageQuery(String hql, Object[] params, IPagination page) {
		List result = find(hql, params, page);
		page.setPageSet(result);
		return page;
	}
	
	
	@Override
	public IPagination pageQuery(String sql, List params) {
		IPagination page = getPageParams();
		return pageQuery(sql, params, page);
	}
	
	@Override
	public IPagination pageQuery(String sql, List params, Class clazz) {
		 IPagination page = getPageParams();
		 return pageQuery(sql, params, page, clazz);
	}
	
	@Override
	public IPagination pageQuery(String sql, List paramList,IPagination page){
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		Connection conn = getConnection();
		
		String countSql = BaseDaoUtil.generateCountSQL(sql);
		try {
			
			pstmt1 = conn.prepareStatement(countSql);

			int pageSize = page.getPageSize();
			int paramSize = 0;
			if (null != paramList) {
				paramSize = paramList.size();
				Object val = null;
				for (int i = 0; i < paramSize; i++) {
					val = paramList.get(i);
					if (val instanceof java.sql.Date) {
						pstmt1.setDate(i + 1, (java.sql.Date) val);
					} else if (val instanceof java.sql.Timestamp){
						pstmt1.setTimestamp(i + 1, (Timestamp) val);
					} else if (val instanceof java.util.Date){
						pstmt1.setDate(i + 1, new java.sql.Date(((java.util.Date) val).getTime()));
					} else {
						pstmt1.setObject(i + 1, val);
					}
				}
			}
			// 分页信息查询
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				page.setMaxCount(rs1.getInt(1));
				int maxPage = rs1.getInt(1) % pageSize == 0 ? rs1.getInt(1) / pageSize
						: rs1.getInt(1) / pageSize + 1;
				page.setMaxPage(maxPage);
				if(page.getPage() >= maxPage){
					page.setPage(maxPage);
				}
			}
			
			String queryString = this.generatePageQuerySQL(sql, page);
			
			pstmt = conn.prepareStatement(queryString);
			
			if (null != paramList) {
				paramSize = paramList.size();
				Object val = null;
				for (int i = 0; i < paramSize; i++) {
					val = paramList.get(i);
					if (val instanceof java.sql.Date) {
						pstmt.setDate(i + 1, (java.sql.Date) val);
					} else if (val instanceof java.sql.Timestamp){
						pstmt.setTimestamp(i + 1, (Timestamp) val);
					} else if (val instanceof java.util.Date){
						pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) val).getTime()));
					} else {
						pstmt.setObject(i + 1, val);
					}
				}
			}
			// 分页参数设置
			pstmt.setInt(paramSize + 1, (page.getPage()) * pageSize);
			pstmt.setInt(paramSize + 2, (page.getPage() - 1) * pageSize);

			// 分页查询
			rs = pstmt.executeQuery();

			// 分页查询结果处理
			List ls = (List) RsBeanUtil.rsToMap(rs);
			page.setPageSet(ls);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
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
	public IPagination pageQuery(String sql, List paramList, IPagination page, Class clazz) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		Connection conn = getConnection();
		
		String countSql = BaseDaoUtil.generateCountSQL(sql);
		try {
			pstmt1 = conn.prepareStatement(countSql);

			int pageSize = page.getPageSize();
			int paramSize = 0;
			if (null != paramList) {
				paramSize = paramList.size();
				Object param = null;
				for (int i = 0; i < paramSize; i++) {
					param = paramList.get(i);
					if(param instanceof Timestamp){
						Timestamp timestamp = (Timestamp)param;
						pstmt1.setTimestamp(i + 1, timestamp);
						
					} else if (param instanceof java.sql.Timestamp) {
						pstmt1.setTimestamp(i + 1, (Timestamp) param );
						
					} else if(param instanceof Date) {
						long time = ((java.util.Date) param).getTime();
						java.sql.Date dateTime = new java.sql.Date(time);
						pstmt1.setDate(i + 1, dateTime);
						
					}  else{
						pstmt1.setObject(i + 1, param);
					}
				}
			}
			// 分页信息查询
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				page.setMaxCount(rs1.getInt(1));
				int maxPage = rs1.getInt(1) % pageSize == 0 ? rs1.getInt(1) / pageSize
						: rs1.getInt(1) / pageSize + 1;
				page.setMaxPage(maxPage);
				if(page.getPage() >= maxPage){
					page.setPage(maxPage);
				}
			}
			String queryString = this.generatePageQuerySQL(sql, page);
			
			pstmt = conn.prepareStatement(queryString);
			
			if (null != paramList) {
				paramSize = paramList.size();
				Object param = null;
				for (int i = 0; i < paramSize; i++) {
					param = paramList.get(i);
					if(param instanceof Timestamp){
						Timestamp timestamp = (Timestamp)param;
						pstmt.setTimestamp(i + 1, timestamp);
						
					} else if (param instanceof java.sql.Timestamp) {
						pstmt.setTimestamp(i + 1, (Timestamp) param);
						
					} else if(param instanceof Date) {
						long time = ((java.util.Date) param).getTime();
						java.sql.Date dateTime = new java.sql.Date(time);
						pstmt.setDate(i + 1, dateTime);
						
					}  else{
						pstmt.setObject(i + 1, param);
					}
				}
			}
			
			// 分页参数设置
			pstmt.setInt(paramSize + 1, (page.getPage()) * pageSize);
			pstmt.setInt(paramSize + 2, (page.getPage() - 1) * pageSize);

			// 分页查询
			rs = pstmt.executeQuery();

			// 分页查询结果处理
			List ls = (List) RsBeanUtil.rsToBean(rs, clazz);
			page.setPageSet(ls);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
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
	public IPagination pageQueryWith(String statement, Object parameter) {
		IPagination pageParams = getPageParams();
		return pageQueryWith(statement, parameter, pageParams);
	}

	
	@Override
	public IPagination pageQueryWith(String statement, Object parameter,
			IPagination page) {
		int pageSize = page.getPageSize();
		int pageIndex = page.getPage();
		if(pageIndex <= 0){
			pageIndex = 1;
		}
		int offset = (pageIndex - 1) * pageSize;
		int limit = pageSize;
		
		// 添加线程变量，将自动排序参数传递到方言
		ThreadPool.add("EASYWEB_PAGE_QUERY_PAGE_OBJECT", page);
		List<Object> list = sqlSession.selectList(statement, parameter, new RowBounds(offset, limit));
		page.setPageSet(list);
		
		// 通过线程变量共享该属性，待压力测试验证
		Object totalItems = ThreadPool.get("EASYWEB_PAGE_QUERY_TOTAL_ITEMS");
		if(totalItems != null){
			int iTotalItems = (Integer) totalItems;
			page.setMaxCount(iTotalItems);
			int maxPage = (iTotalItems % pageSize == 0) ? (iTotalItems / pageSize) : (iTotalItems / pageSize + 1);
			page.setMaxPage(maxPage);
		}
		return page;
	}

	
	/**
	 * 根据查询语句和分页参数生成分页查询语句
	 * @param sql
	 * @param page
	 * @return
	 */
	public abstract String generatePageQuerySQL(String sql,IPagination page);
	
	
	/**
	 * 获取分页信息对象
	 * 
	 * @param request
	 * @return
	 */
	public IPagination getPageParams() {
		HttpServletRequest request = (HttpServletRequest) ThreadPool.get(SysConstant.HTTP_REQUEST);
		IPagination pageInfo = (IPagination) new Pagination();
		String pageNum = request.getParameter("pageNum");
		String numPerPage = request.getParameter("numPerPage");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		
		if(StringUtil.isNotNull(pageNum)){
			pageInfo.setPage(Integer.parseInt(pageNum));
		}
		if(StringUtil.isNotNull(numPerPage)){
			pageInfo.setPageSize(Integer.parseInt(numPerPage));
		}
		pageInfo.setOrderField(orderField);
		pageInfo.setOrderDirection(orderDirection);
		return pageInfo;
	}
	
}
