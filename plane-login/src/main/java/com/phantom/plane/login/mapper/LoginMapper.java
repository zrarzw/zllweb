package com.phantom.plane.login.mapper;

import com.phantom.plane.login.pojo.UserBO;
import com.phantom.plane.sys.pojo.User;

import tk.mybatis.mapper.common.Mapper;

public interface LoginMapper extends Mapper<UserBO>{
	 public UserBO selectByUser(UserBO user);
	 public void insertdemo(UserBO user);
}
