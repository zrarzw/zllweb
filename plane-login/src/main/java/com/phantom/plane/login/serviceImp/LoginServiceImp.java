/**
 * 
 */
package com.phantom.plane.login.serviceImp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.phantom.plane.login.pojo.UserBO;
import com.phantom.plane.login.service.LoginService;

/**
* <p>project:plane-login</p>
* <p>Description: </p>
* <p>Company: </p> 
* @author zw
* @date 2017年9月4日上午10:36:21
*/
@Service
public class LoginServiceImp implements LoginService{
	
	@Resource
	
	private Logger logger = Logger.getLogger(LoginServiceImp.class);

	 public UserBO selectByUser(UserBO user) {
		 logger.info("输出info");
		
		 logger.debug("输出debug");
	
			logger.error("输出error");
		 System.out.println(this.getClass().getName());
		
	}
}
