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
package com.phantom.plane.core.interfacer;

import org.slf4j.Logger;

/**
 * 日志接口
 */
public interface ILogger {
	
	void info(String s,Object...args);
	
	void debug(String s,Object...args);
	
	void error(String s,Object...args);
	
	void error(String s);
	
	void warn(String s,Object...args);
	
	Logger getLogger();
	
	boolean isInfoEnabled();
	
	boolean isDebugEnabled();
	
	boolean isErrorEnabled();
}
