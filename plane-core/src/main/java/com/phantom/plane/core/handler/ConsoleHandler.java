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
import com.phantom.plane.core.exception.ExceptionUtil;
import com.phantom.plane.core.exception.IAppResponse;

/**
 * 异常控制台处理器(Console handling)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 * @see IExceptionHandler
 */
public  class ConsoleHandler implements IExceptionHandler{
	
	public Object handler(BaseException e,IAppResponse response) {
		if(null == e) return null;
		Throwable base = ExceptionUtil.getExceptionUtil().getRootCause(e);
		String errorMessage = base.getMessage()==null ? base.getClass().toString() : base.getMessage();
		
		response.setErrorCode(String.valueOf(e.getErrorCode()));
		response.setMessage(errorMessage);
		response.setSuccess(false);
		
		System.out.println(response.getMessage());
		return response;
	}

	
}
