/*
oa* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-22
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

import com.phantom.plane.core.annotation.PhException;
import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.handler.Log4jHandler;

/**
 * 持久异常(Persistence Exception)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 * @see BaseException
 */
@PhException(errorCode = 3000, handlers = { Log4jHandler.class})
public class PersistenceException extends BaseException{

	private static final long serialVersionUID = -4341254142137917077L;
	
	public PersistenceException(){
		super();
	}
	
	public PersistenceException(int errorCode){
		super(errorCode);
	}
	
	public PersistenceException(int errorCode, String message){
		super(errorCode, message);
	}
	
	public PersistenceException(String message){
		super(message);
	}
	
	public PersistenceException(Throwable cause){
		super(cause);
	}
	
	public PersistenceException(String message,Throwable cause){
		super(message,cause);
	}
	
	public PersistenceException(int errorCode,Throwable cause){
		super(errorCode, cause);
	}
}
