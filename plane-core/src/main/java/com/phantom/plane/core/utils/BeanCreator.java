/**
 * Project:				easyweb-core
 * Author:				GreenZHAO
 * Company: 			杭州中软
 * Created Date:		2013-3-18
 * Description:			
 * Copyright @ 2013 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.utils;

import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.jdbc.core.SqlTypeValue;

import com.phantom.plane.core.base.BaseException;



public class BeanCreator {
	public static final int DATA_FROM_WEB = 1;
	public static final int DATA_FROM_DB = 2;
	
	public static <T> T create(Class<T> clazz,Map<String,Object> map,int from) throws BaseException{
		Iterator<String> iterator = map.keySet().iterator();
		// 临时变量,如果存在返回对象，将设置在该变量上
		Object tempObject = initBean(clazz);
		
		String paramName;
		String realFieldName;
		boolean hasInjectValue = false;
		boolean isNormalProperty = true;
		PropertyDescriptor descriptor = null;
		// 返回值类型
		Class type;
		while(iterator.hasNext()){
			paramName = iterator.next().toString();
			realFieldName = paramName;
			
			Object val = map.get(paramName);
			if(null == val || String.valueOf(val).length() == 0) continue;
			
			// 数据来自数据库提取
			// 转换成实例类对应属性，主要需要对下划线属性转换成实例类对应属性名
			if(BeanCreator.DATA_FROM_DB == from){
				realFieldName = getFieldName(realFieldName,tempObject);
				if(StringUtil.isNull(realFieldName)){
					continue;
				}
			}
			
			// 检查是否是普通属性
			// 主要检查属性名中是否包含逗号分隔的属性，逗号分隔表示该属性是一个实例属性
			isNormalProperty = isNormalProperty(realFieldName);
			
			try {
				// 普通属性注入
				if(isNormalProperty){
					descriptor = PropertyUtils.getPropertyDescriptor(tempObject, realFieldName);
					// 1、不存在setter方法，忽略
					if(null == descriptor){
						continue;
					}
					// 2、数组类型、忽略
					type = descriptor.getReadMethod().getReturnType();
					if(type.isArray()){
						continue;
					}
					// String sourceValue = map.get(paramName).toString();
					// 转换原始参数值
					Object convertValue = convertValue(type,val);
					// 反射设置目标对象
					descriptor.getWriteMethod().invoke(tempObject, convertValue);
					// 设置属性注入标识
					hasInjectValue = true;
					
				// 对象属性注入
				}else{
					String sourceValue = map.get(paramName).toString();
					String[] classProperties = paramName.split("\\.");
					int len = classProperties.length;
					
					// 方法操作对象队列 
					// jdk1.6
					Deque<PropertyDescriptor> descriptorDeque = new ArrayDeque<PropertyDescriptor>();
					
					// 临时值队列
					// jdk1.6
					Deque<Object> valueDeque = new ArrayDeque<Object>();
					valueDeque.push(tempObject);
					
					Object temp = tempObject;
					for (int i = 0; i < len; i++) {
						descriptor = PropertyUtils.getPropertyDescriptor(temp, classProperties[i]);
						// 1、不存在setter方法，忽略
						if(null == descriptor){
							descriptorDeque.clear();
							break;
						}
						type = descriptor.getReadMethod().getReturnType();
						// 2、数组类型、忽略
						if(type.isArray()){
							descriptorDeque.clear();
							break;
						}
						// jdk1.6
						descriptorDeque.push(descriptor);
						
						if(i != len-1){
							Object invoke = descriptor.getReadMethod().invoke(temp);
							if(invoke != null){
								temp = invoke;
							}else{
								temp = initBean(type);
							}
							// jdk1.6
							valueDeque.push(temp);
						}
					}
					
					Object value = sourceValue;
					boolean hasInjectedObject = false;
					while(descriptorDeque.size()>0){
						// jdk1.6
						tempObject = valueDeque.pop();
						descriptorDeque.pop().getWriteMethod().invoke(tempObject, value);
						
						// jdk1.5
//						tempObject = valueDeque.poll();
//						descriptorDeque.poll().getWriteMethod().invoke(tempObject, value);
						
						value = tempObject;
						// 设置属性注入标识
						hasInjectValue = true;
						hasInjectedObject=true;
					}
					if(hasInjectedObject){
						tempObject = value;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		// 根据变量可以控制如果没有属性值注入，是否返回null对象
		// 去除null值返回
//		if(false == hasInjectValue){
//			return null;
//		}
		return (T) tempObject;
	}
	
	/**
	 * Check whether the given value can be treated as a String value.
	 */
	public static boolean isStringValue(Class inValueType) {
		// Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
		return (CharSequence.class.isAssignableFrom(inValueType) ||
				StringWriter.class.isAssignableFrom(inValueType));
	}
	
	
	public static boolean isNumberValue(Class inValueType){
		return Number.class.isAssignableFrom(inValueType);
	}

	/**
	 * Check whether the given value is a <code>java.util.Date</code>
	 * (but not one of the JDBC-specific subclasses).
	 */
	public static boolean isDateValue(Class inValueType) {
		return (java.util.Date.class.isAssignableFrom(inValueType) &&
				!(java.sql.Date.class.isAssignableFrom(inValueType) ||
						java.sql.Time.class.isAssignableFrom(inValueType) ||
						java.sql.Timestamp.class.isAssignableFrom(inValueType)));
	}
	
	
	public static boolean isTimeValue(Class inValueType){
		return (java.util.Date.class.isAssignableFrom(inValueType) && 
				java.sql.Time.class.isAssignableFrom(inValueType));
	}
	
	public static boolean isTimeStampValue(Class inValueType){
		return (java.util.Date.class.isAssignableFrom(inValueType) && 
				java.sql.Timestamp.class.isAssignableFrom(inValueType));
	}
	
	public static String getFieldName(String columnName,Object bean){
		StringBuffer sb = new StringBuffer();
		sb.append(columnName.trim().toLowerCase());
		
		int c = sb.indexOf("_");
		int len = sb.length();
		
		String fieldName = null;
		// 含下划线
		if(c >0){
			while (c != 0) {
				int num = sb.indexOf("_", c);
				c = num + 1;
				if (num != -1 && c <len) {
					char ss = sb.charAt(c);
					char ia = (char) (ss - 32);
					sb.replace(c, c + 1, ia + "");
				}
			}
			fieldName = sb.toString().replaceAll("_", "");
			
		// 无下划线
		} else {
//			String temp = columnName.toLowerCase();
//			// 使用子查询方式查找实体属性名
//			Field[] fields = bean.getClass().getDeclaredFields();
//			for(Field f : fields){
//				if(temp.equals(f.getName().toLowerCase())){
//					fieldName = f.getName();
//					break;
//				}
//			}
			
			// 使用查找属性确定字段真实定义有局限，无法获取父类定义的字段信息
			// 改为通过set方法判断具体字段名称
			Method[] methods =  bean.getClass().getMethods();
			StringBuffer setterName = new StringBuffer("set").append(columnName.toLowerCase());
			
			String declaredField = "";
			for(Method method : methods){
				String methodName = method.getName();
				if(setterName.toString().equals(methodName.toLowerCase())){
					String tempFieldName = methodName.substring(3);
					char[] charArray = tempFieldName.toCharArray();
					
					// 检查第二个字母是否大写,如果是大写，直接返回字段名
					// 如果没有大写，那么首字母小写后返回字段名
					if(charArray.length >= 2 && Character.isUpperCase(charArray[1])){
						declaredField = tempFieldName;
						break;
					}else{
						// 长度小于1时，或者第二个字母非大写时，将首字母小写后返回
						charArray[0] = Character.toLowerCase(charArray[0]);
						declaredField = new String(charArray);
						break;
					}
				}
			}
			fieldName = declaredField;
		}
		return fieldName;
	}
	
	
	public static int[] getArgTypes(List params){
		int len = params.size();
		int[] argTypes = new int[len];
		Object val = null;
		for (int i = 0; i < len; i++) {
			val = params.get(i);
			if(BeanCreator.isStringValue(val.getClass())){
				argTypes[i] = java.sql.Types.VARCHAR;
			}else if(BeanCreator.isNumberValue(val.getClass())){
				argTypes[i] = java.sql.Types.NUMERIC;
			}else if(BeanCreator.isDateValue(val.getClass())){
				argTypes[i] = java.sql.Types.DATE;
			}else if(BeanCreator.isTimeValue(val.getClass())){
				argTypes[i] = java.sql.Types.TIME;
			}else if(BeanCreator.isTimeStampValue(val.getClass())){
				argTypes[i] = java.sql.Types.TIMESTAMP;
			}else{
				argTypes[i] = SqlTypeValue.TYPE_UNKNOWN;
			}
		}
		return argTypes;
	}
	
	
	
	/**
	 * 检查是否为普通属性
	 * @param parameterName
	 * @return
	 */
	private static boolean isNormalProperty(String parameterName){
		return !(parameterName.indexOf(".")>0);
	}
	
	
	/**
	 * 使用类的默认构造方法初始化
	 * @param clazz
	 * @return
	 * @throws BaseException 
	 */
	private static <T> T initBean(Class<T> clazz) throws BaseException{
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new BaseException(1009);
		} catch (IllegalAccessException e) {
			throw new BaseException(1003);
		}
	}

	
	/**
	 * 值转换
	 * @param type 			转换目标类型
	 * @param sourceValue	原始值
	 * @return
	 * @throws ParseException 
	 */
	private static Object convertValue(Class type,Object sourceValue) throws ParseException{
		// 类型相同，无须转换
		if(type.isAssignableFrom(sourceValue.getClass())){
			return sourceValue;
		}
		Object tempValue = "";
		String returnTypeClassName = type.getName();
		// 类型不一致，做转换后返回
		String sSourceValue = String.valueOf(sourceValue);
		if (returnTypeClassName.equalsIgnoreCase("java.lang.String")) {
			tempValue = sSourceValue;
		} else if (returnTypeClassName.equalsIgnoreCase("long")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Long")) {
			tempValue = new Long(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("int")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Integer")) {
			tempValue = new Integer(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("short")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Short")) {
			tempValue = new Short(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("float")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Float")) {
			tempValue = new Float(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("double")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Double")) {
			tempValue = new Double(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("boolean")
				|| returnTypeClassName.equalsIgnoreCase("java.lang.Boolean")) {
			tempValue = new Boolean(sSourceValue);
		} 	else if (returnTypeClassName.equalsIgnoreCase("java.math.BigDecimal")) {
			tempValue = new BigDecimal(sSourceValue);
		} else if (returnTypeClassName.equalsIgnoreCase("java.util.Date")
				|| returnTypeClassName.equalsIgnoreCase("java.sql.Date")) {
				// yyyy-MM-dd
			if (sSourceValue.length() == 10) {
				tempValue = new SimpleDateFormat("yyyy-MM-dd").parse(sSourceValue);
				// yyyy-MM
			} else if (sSourceValue.length() == 7) {
				tempValue = new SimpleDateFormat("yyyy-MM").parse(sSourceValue);
				// yyyy
			} else if (sSourceValue.length() == 4) {
				tempValue = new SimpleDateFormat("yyyy").parse(sSourceValue);
				// yyyy-MM-dd HH:mm:ss
			} else if (sSourceValue.length() == 19) {
				tempValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") .parse(sSourceValue);
				// yyyy-MM-dd HH:mm
			} else if (sSourceValue.length() == 16) {
				tempValue = new SimpleDateFormat("yyyy-MM-dd HH:mm") .parse(sSourceValue);
				// yyyy-MM-dd HH
			} else if (sSourceValue.length() == 13) {
				tempValue = new SimpleDateFormat("yyyy-MM-dd HH") .parse(sSourceValue);
			} else if (sSourceValue.length() > 19) {
				tempValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") .parse(sSourceValue);
			}
		}  else if (returnTypeClassName.equalsIgnoreCase("char")) {
			tempValue = (Character.valueOf(sSourceValue.charAt(0)));
		}else if(returnTypeClassName.equalsIgnoreCase("java.sql.Blob")){
			tempValue = null;
		}
		return tempValue;
	}
	
	
}
