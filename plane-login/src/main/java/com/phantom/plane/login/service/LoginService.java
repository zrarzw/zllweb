/**
 * 
 */
package com.phantom.plane.login.service;

import com.phantom.plane.core.base.BaseService;
import com.phantom.plane.login.pojo.UserBO;

/**
* <p>project:plane-login</p>
* <p>Description: </p>
* <p>Company: </p> 
* @author zw
* @date 2017年9月4日上午10:35:37
*/
public interface LoginService {
	 public UserBO selectByUser(UserBO user);
}
