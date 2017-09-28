package com.phantom.plane.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.exception.ExceptionUtil;
import com.phantom.plane.core.exception.IAppResponse;


public class Log4jHandler implements IExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(Log4jHandler.class);
	
	public Object handler(BaseException e, IAppResponse response) {
		if(null == e) return null;
		Throwable base = ExceptionUtil.getExceptionUtil().getRootCause(e);
		String errorMessage = base.getMessage()==null ? base.getClass().toString() : base.getMessage();
		
		response.setErrorCode(String.valueOf(e.getErrorCode()));
		response.setMessage(errorMessage);
		response.setSuccess(false);
		
		logger.error(errorMessage, e);
		
		// 同时输出到控制台，避免异常情况下控制台无异常的问题
		e.printStackTrace();
		return response;	
	}
	
}
