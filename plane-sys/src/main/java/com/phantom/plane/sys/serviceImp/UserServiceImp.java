/*package com.phantom.plane.sys.serviceImp;



import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phantom.plane.core.mybatis.MapperClass;
import com.phantom.plane.core.mybatis.MyBatisBaseServiceImpl;
import com.phantom.plane.sys.mapper.UserMapper;
import com.phantom.plane.sys.pojo.User;


@Service
@MapperClass(UserMapper.class)
public class UserServiceImp extends MyBatisBaseServiceImpl<User> implements UserMapper {
	@Autowired
	private UserDao userDaoImp;	
	
    protected UserMapper getMapper(){
        return super.getMapper(User.class);
    }

    
	@Transactional
	 public User selectByUser(User user){
		return  getMapper().selectByUser(user);
		 
	 }
	@Override
	public User selectOne(User record) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> select(User record) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int selectCount(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public User selectByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int insertSelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateByPrimaryKeySelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int delete(User record) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<User> selectByExample(Object example) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int selectCountByExample(Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int deleteByExample(Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateByExample(User record, Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateByExampleSelective(User record, Object example) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<User> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> selectByRowBounds(User record, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}


	 (non-Javadoc)
	 * @see com.phantom.plane.sys.mapper.UserMapper#insertdemo(com.phantom.plane.sys.pojo.User)
	 
	@Override
	public void insertdemo(User user) {
		// TODO Auto-generated method stub
		
	}
}
*/