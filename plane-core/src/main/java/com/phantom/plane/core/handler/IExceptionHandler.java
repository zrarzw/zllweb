/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	08/19/2011
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.handler;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.exception.IAppResponse;

/**
 * 系统异常处理器接口(Exceptions Handler Interface)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public interface IExceptionHandler {
	
	Object handler(BaseException e,IAppResponse response);
	
}
