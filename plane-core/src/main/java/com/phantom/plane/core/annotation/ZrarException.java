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

package com.phantom.plane.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.phantom.plane.core.handler.IExceptionHandler;




/**
 * 系统自定义异常注解(user define exception annotation)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZrarException {

	int errorCode();

	Class<? extends IExceptionHandler>[] handlers();

}
