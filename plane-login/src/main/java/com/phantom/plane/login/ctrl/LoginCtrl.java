/**
 * 
 */
package com.phantom.plane.login.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phantom.plane.login.pojo.UserBO;
import com.phantom.plane.login.service.LoginService;

/**
* <p>project:plane-login</p>
* <p>Description: </p>
* <p>Company: </p> 
* @author zw
* @date 2017年9月1日下午5:19:53
*/
@Controller
@RequestMapping("/loginCtrl")
public class LoginCtrl {
	
	@Autowired
	private LoginService loginServiceServiceImp;		
	 @RequestMapping(value="/login",method = RequestMethod.GET)
	 @ResponseBody
    public UserBO login(Map<String,Object> map){
		 UserBO user = new UserBO();
		 user.setId(1);
		 user=loginServiceServiceImp.selectByUser(user);
		 map.put("hello", "好想你");
    	return user;
    }
}
