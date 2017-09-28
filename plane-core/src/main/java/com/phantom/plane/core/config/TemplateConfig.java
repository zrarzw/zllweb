/**
 * 
 */
package com.phantom.plane.core.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.phantom.plane.core.mybatis.MyBatisBaseDaoImpl;

/**
 * <p>
 * project:plane-core
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zw
 * @date 2017年9月22日下午6:03:48
 */
@Configuration
public class TemplateConfig {
	@Resource
	@Qualifier("sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;

	@Resource
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Resource
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	private SqlSession sqlSession;

	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate JdbcTemplate = new JdbcTemplate();
		JdbcTemplate.setDataSource(dataSource);
		return JdbcTemplate;
	}

	public HibernateTemplate hibernateTemplate() {
		HibernateTemplate hibernateTemplate = new HibernateTemplate();
		hibernateTemplate.setSessionFactory(sessionFactory);
		return hibernateTemplate;
	}

	public SqlSession getSqlSession() {
		if (null == sqlSession) {
			synchronized (MyBatisBaseDaoImpl.class) {
				this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
			}
		}
		return this.sqlSession;
	}
}
