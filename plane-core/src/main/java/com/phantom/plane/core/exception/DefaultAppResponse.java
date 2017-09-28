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
 * 异常处理反馈对象(Exception Response)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public class DefaultAppResponse implements IAppResponse {
	private boolean isSuccess;		// 是否无异常
	private String errorCode;		// 错误编码
	private String message;			// 错误消息
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
