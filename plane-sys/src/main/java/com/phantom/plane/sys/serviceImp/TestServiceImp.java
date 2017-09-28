/*package com.phantom.plane.sys.serviceImp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phantom.plane.sys.dao.TestDao;
import com.phantom.plane.sys.pojo.User;
import com.phantom.plane.sys.service.TestService;
@Service
public class TestServiceImp implements TestService{
	@Resource
	private TestDao testDaoImp;		
@Override
@Transactional
public User test(User user) {
	// TODO Auto-generated method stub
	return testDaoImp.selectByUser(user);
	
}
@Override
@Transactional
public void demo(User user){
	testDaoImp.insertdemo(user);
}

}
*/