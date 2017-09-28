/*package com.phantom.plane.sys.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phantom.plane.sys.mapper.UserMapper;
import com.phantom.plane.sys.pojo.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

*//**
 * Created by wangwei on 2016/9/2.
 *//*
@RestController
@RequestMapping({"/user"})
@Api(value = "user", description = "用户管理", produces = MediaType.APPLICATION_JSON_VALUE) 
public class UserCtrl {
	
	@Autowired
	private UserMapper userServiceImp;	
	
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value="/findDemoDtos", method=RequestMethod.GET)
    public String findDemoDtos() {
 
        User us = new User();
        us.setId(1);   
        us= userServiceImp.selectByUser(us);
        return "success";
    }

}*/