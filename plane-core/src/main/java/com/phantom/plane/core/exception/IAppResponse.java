/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-21
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

/**
 * 异常处理反馈对象接口(Exception Response Interface)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public interface IAppResponse {
	
	boolean isSuccess();
	
	String getErrorCode();
	
	String getMessage();
	
	void setSuccess(boolean isSuccess);
	
	void setErrorCode(String errorCode);
	
	void setMessage(String message);
	
}
