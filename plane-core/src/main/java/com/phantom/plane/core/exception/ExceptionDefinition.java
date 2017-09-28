/*
* Project:		
* Author:		赵志武
* Company: 		杭州中软
* Created Date:	2011-08-19
* Copyright @ 2011 CS&S.COM - Confidential and Proprietary
* 
* History:
* ------------------------------------------------------------------------------
* Date			|time		|Author	|Change Description		*/
package com.phantom.plane.core.exception;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 异常定义(Exception Definition Bean)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public class ExceptionDefinition {
	private String errorCode;
	private Set<String> handlerNames = new LinkedHashSet<String>();
	
	public ExceptionDefinition(String errorCode){
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Set<String> getHandlerNames() {
		return handlerNames;
	}
}
