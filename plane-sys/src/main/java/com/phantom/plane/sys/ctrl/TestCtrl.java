/*package com.phantom.plane.sys.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.phantom.plane.sys.pojo.User;
import com.phantom.plane.sys.service.TestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

*//**
 * Created by wangwei on 2016/9/2.
 *//*
@RestController
@RequestMapping({"/testCtrl"})
@Api(value = "user", description = "用户管理", produces = MediaType.APPLICATION_JSON_VALUE) 
public class TestCtrl {
	
	@Autowired
	private TestService testServiceImp;	
	
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value="/test1", method=RequestMethod.GET)
    public String test1() {
        User us = new User();
        us.setId(2);   
        us= testServiceImp.test(us);
        return "success";
    }
    
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value="/test2", method=RequestMethod.GET)
    public String test2() {
        User us = new User();
        us.setAccount("xmj");
        us.setName("乡妹几");
        us.setAge(12);
        testServiceImp.demo(us);
        return "success";
    }

}*/