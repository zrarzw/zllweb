/**
 * 
 *//*
package com.phantom.plane.sys.daoImp;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.phantom.plane.sys.dao.TestDao;
import com.phantom.plane.sys.pojo.User;

*//**
* <p>project:plane-sys</p>
* <p>Description: </p>
* <p>Company: </p> 
* @author zw
* @date 2017年8月30日下午4:31:30
*//*
@Repository
public class TestJdbcT extends JdbcDaoSupport implements TestDao {
	//@Autowired
	//private JdbcTemplate jdbcTemplate;  
	public void insertJDBC(User user) {
		
		;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.select.SelectOneMapper#selectOne(java.lang.Object)
	 
	@Override
	public User selectOne(User record) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.select.SelectMapper#select(java.lang.Object)
	 
	@Override
	public List<User> select(User record) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.select.SelectAllMapper#selectAll()
	 
	@Override
	public List<User> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.select.SelectCountMapper#selectCount(java.lang.Object)
	 
	@Override
	public int selectCount(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper#selectByPrimaryKey(java.lang.Object)
	 
	@Override
	public User selectByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.insert.InsertMapper#insert(java.lang.Object)
	 
	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper#insertSelective(java.lang.Object)
	 
	@Override
	public int insertSelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper#updateByPrimaryKey(java.lang.Object)
	 
	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper#updateByPrimaryKeySelective(java.lang.Object)
	 
	@Override
	public int updateByPrimaryKeySelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.delete.DeleteMapper#delete(java.lang.Object)
	 
	@Override
	public int delete(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper#deleteByPrimaryKey(java.lang.Object)
	 
	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.example.SelectByExampleMapper#selectByExample(java.lang.Object)
	 
	@Override
	public List<User> selectByExample(Object example) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.example.SelectCountByExampleMapper#selectCountByExample(java.lang.Object)
	 
	@Override
	public int selectCountByExample(Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.example.DeleteByExampleMapper#deleteByExample(java.lang.Object)
	 
	@Override
	public int deleteByExample(Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.example.UpdateByExampleMapper#updateByExample(java.lang.Object, java.lang.Object)
	 
	@Override
	public int updateByExample(User record, Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.example.UpdateByExampleSelectiveMapper#updateByExampleSelective(java.lang.Object, java.lang.Object)
	 
	@Override
	public int updateByExampleSelective(User record, Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.rowbounds.SelectByExampleRowBoundsMapper#selectByExampleAndRowBounds(java.lang.Object, org.apache.ibatis.session.RowBounds)
	 
	@Override
	public List<User> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper#selectByRowBounds(java.lang.Object, org.apache.ibatis.session.RowBounds)
	 
	@Override
	public List<User> selectByRowBounds(User record, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see com.phantom.plane.sys.dao.TestDao#find()
	 
	@Override
	public User find() {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see com.phantom.plane.sys.dao.TestDao#selectByUser(com.phantom.plane.sys.pojo.User)
	 
	@Override
	public User selectByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	 (non-Javadoc)
	 * @see com.phantom.plane.sys.dao.TestDao#insertdemo(com.phantom.plane.sys.pojo.User)
	 
	@Override
	public void insertdemo(User user) {
		// TODO Auto-generated method stub
		
	}
	
}
*/