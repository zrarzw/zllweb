/*package com.phantom.plane.sys.daoImp;


import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.phantom.plane.sys.dao.UserDao;
import com.phantom.plane.sys.pojo.User;

@Repository
public class UserDaoImp extends SqlSessionDaoSupport implements UserDao{
@Resource
	    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	        // TODO Auto-generated method stub
	        super.setSqlSessionFactory(sqlSessionFactory);
	    }
 
    public void saveUser(User user){
    	System.out.println("1111111111111111111");
    	// this.getSqlSession().selectOne(this.getClass().getName() + ".saveUser", user);
    }

	@Override
	public User selectByUser(User user) {
		// TODO Auto-generated method stub
	 return this.selectByUser(user);
	}
}
*/