/**
 * Project:				easyweb-core
 * Author:				GreenZHAO
 * Company: 			杭州中软
 * Created Date:		2012-11-2
 * Description:			
 * Copyright @ 2012 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.common;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phantom.plane.core.interfacer.ILogger;



public class ZrarLogger implements ILogger, Serializable {
	
	private static final long serialVersionUID = -4430855194417471015L;
	
	private static Logger logger = LoggerFactory.getLogger(ZrarLogger.class);
	
	public static ZrarLogger getInstance(){
		return Instance.logger;
	}
	
	private static class Instance{
		private static ZrarLogger logger = new ZrarLogger();
	}

	@Override
	public void info(String s, Object... args) {
		logger.info(s, args);
	}

	@Override
	public void debug(String s, Object... args) {
		logger.debug(s, args);
		
	}

	@Override
	public void error(String s, Object... args) {
		logger.error(s, args);
	}

	@Override
	public void warn(String s, Object... args) {
		logger.warn(s, args);
	}

	@Override
	public void error(String s) {
		logger.error(s);
	}
	
	
	
	public Logger getLogger(){
		return logger;
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}
}
