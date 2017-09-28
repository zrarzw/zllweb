
/*
 * Project:		
 * Author:			赵志武
 * Company: 		杭州中软
 * Created Date:	08/19/2011
 * Copyright @ 2011 CS&S.COM - Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.base;

import com.phantom.plane.core.annotation.PhException;
import com.phantom.plane.core.common.SysConstant;
import com.phantom.plane.core.handler.Log4jHandler;
import com.phantom.plane.core.utils.PropertyManager;
import com.phantom.plane.core.utils.StringUtil;


/**
 * 系统级异常(Base Runtime(UnCheck) Exception )
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 * @see RuntimeException
 */
@PhException(errorCode = 1000, handlers = { Log4jHandler.class})
public class BaseException extends Exception{
	private static final long serialVersionUID = -1629181389032548626L;
	
	protected Throwable cause;
	protected int errorCode = 0;
	public static final int NOT_HANDLERD = 0;

	public BaseException(){
		super();
		errorCode = this.getClass().getAnnotation(PhException.class).errorCode();
	}
	
	public BaseException(int errorCode){
		super();
		this.errorCode = errorCode;
	}
	
	public BaseException(int errorCode, String message){
		super(message);
		this.errorCode = errorCode;
	}
	
	public BaseException(String message){
		super(message);
	}
	
	public BaseException(Throwable cause){
		super(cause);
		this.cause = cause;
	}
	
	public BaseException(String message,Throwable cause){
		super(message,cause);
		this.cause = cause;
	}
	
	public BaseException(int errorCode,Throwable cause){
		super(cause);
		this.errorCode = errorCode;
		this.cause = cause;
	}

	public Throwable getCause(){
		return this.cause;
	}
	
	public int getErrorCode(){
		return this.errorCode;
	}
	public int handlerErrorCode() {
		return NOT_HANDLERD;
	}
	public String getMessage(){
		String errorMsg = PropertyManager.getProperty(
				String.valueOf(errorCode), SysConstant.FILE_ERROR_CONFIG);
		errorMsg = StringUtil.null2Str(errorMsg);
		String superMessage = super.getMessage();
		errorMsg += (" "+ (StringUtil.isNotNull(superMessage)? superMessage : ""));
		return errorMsg;
	}
}
