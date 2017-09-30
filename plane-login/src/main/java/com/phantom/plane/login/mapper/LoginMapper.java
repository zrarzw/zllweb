package com.phantom.plane.login.mapper;

import com.phantom.plane.login.pojo.UserBO;

import tk.mybatis.mapper.common.Mapper;

public interface LoginMapper extends Mapper<UserBO>{
	 public UserBO selectByUser(UserBO user);
	 public void insertdemo(UserBO user);
}
