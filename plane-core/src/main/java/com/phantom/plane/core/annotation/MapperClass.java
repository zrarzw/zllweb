
package com.phantom.plane.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Athos on2016-09-04.6767颠三倒四
 */
@SuppressWarnings("unchecked")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapperClass {
	/** 
	 *指定 MapperClass
	 */
    Class value();
}

