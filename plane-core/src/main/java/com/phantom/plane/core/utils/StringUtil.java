package com.phantom.plane.core.utils;
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * 字符串处理工具类(String Utils)
 * 
 * @author 赵志武
 * @version 1.0
 * @since jdk1.5
 */
public class StringUtil {

	/**
	 * 将null转换成空串
	 * 
	 * @param str
	 * @return
	 */
	public static String null2Str(String str) {
		return (str == null) ? "" : str.trim();
	}

	public static String null2Str(Object obj) {
		return (obj == null) ? "" : String.valueOf(obj).trim();
	}

	/**
	 * 判断字符串是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		str = null2Str(str);
		return str.length() == 0;
	}

	/**
	 * 判断是否为非空字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotNull(String s) {
		return !isNull(s);
	}

	/**
	 * 字符串转换成日期类型
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date string2Date(String str) throws ParseException {
		String formatExp = "yyyy-MM-dd";
		if ((str + "").length() > 10)
			formatExp = "yyyy-MM-dd HH:mm:ss";
		DateFormat format = new SimpleDateFormat(formatExp);
		return format.parse(str);
	}

	/**
	 * 将对象转换成指定key的Json字符串
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public static String toJsonString(String key, Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, object);
		JSONObject jsonObject = JSONObject.fromObject(map);
		String s = jsonObject.toString();
		return s.substring(1, s.length() - 1);
	}

	/**
	 * 
	 * 功能：Map按key排序
	 * 
	 * @param map
	 *            :未排序map
	 * @return 排序后map
	 * @throws Exception
	 */
	public static SortedMap<String, String> mapSortByKey(Map<String, String> map) {
		TreeMap<String, String> result = new TreeMap<String, String>();
		if (!map.isEmpty()) {
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			for (int i = 0; i < key.length; i++) {
				result.put(key[i].toString(), map.get(key[i]));
			}
		}
		return result.tailMap(result.firstKey());
	}

	/**
	 * 替换空格 回车 TAB为""
	 * 
	 * @param s
	 * @return
	 */
	public static String replaceBlank(String s) {
		if (StringUtil.isNull(s))
			return "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(s);
		String after = m.replaceAll("");
		return after;
	}

	/**
	 * @param selectStr
	 *            查询字符
	 * @param targetStr
	 *            目标字符串
	 * @return 字符在字符串中出现次数
	 */
	public static int findStrIndexOfCount(String selectStr, String targetStr) {
		int selectLength = selectStr.length();
		int targetLength = targetStr.length();
		if (selectLength > 0 && selectLength < targetLength) {
			return (targetLength - targetStr.replaceAll(selectStr, "").length()) / selectLength;
		}
		return -1;
	}

	/**
	 * 是否纯数字
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNumeric(String s) {
		int i = s.length();
		while (true) {
			i--;
			if (i < 0)
				break;
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}

	/**
	 * 是否为浮点数字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDouble(String s) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(s).matches();
	}

	public static StringBuilder formatMsg(CharSequence paramCharSequence, boolean paramBoolean,
			Object[] paramArrayOfObject) {
		int i = paramArrayOfObject.length;
		int j = 0;
		StringBuilder localStringBuilder = new StringBuilder(paramCharSequence);
		if (i > 0) {
			for (int k = 0; k < i; k++) {
				String str = new StringBuilder().append("%").append(k + 1).toString();
				for (int m = localStringBuilder.indexOf(str); m >= 0; m = localStringBuilder.indexOf(str)) {
					j = 1;
					localStringBuilder.replace(m, m + 2, toString(paramArrayOfObject[k], paramBoolean));
				}
			}
			if ((paramArrayOfObject[(i - 1)] instanceof Throwable)) {
				StringWriter localStringWriter = new StringWriter();
				((Throwable) paramArrayOfObject[(i - 1)]).printStackTrace(new PrintWriter(localStringWriter));
				localStringBuilder.append("\n").append(localStringWriter.toString());
			} else if ((i == 1) && (j == 0)) {
				localStringBuilder.append(paramArrayOfObject[(i - 1)].toString());
			}
		}
		return localStringBuilder;
	}

	public static StringBuilder formatMsg(String paramString, Object[] paramArrayOfObject) {
		return formatMsg(new StringBuilder(paramString), true, paramArrayOfObject);
	}

	public static String toString(Object paramObject, boolean paramBoolean) {
		StringBuilder localStringBuilder = new StringBuilder();
		if (paramObject == null) {
			localStringBuilder.append("NULL");
		} else if ((paramObject instanceof Object[])) {
			for (int i = 0; i < ((Object[]) (Object[]) paramObject).length; i++)
				localStringBuilder.append(((Object[]) (Object[]) paramObject)[i]).append(", ");
			if (localStringBuilder.length() > 0)
				localStringBuilder.delete(localStringBuilder.length() - 2, localStringBuilder.length());
		} else {
			localStringBuilder.append(paramObject.toString());
		}
		if ((paramBoolean) && (localStringBuilder.length() > 0)
				&& ((localStringBuilder.charAt(0) != '[')
						|| (localStringBuilder.charAt(localStringBuilder.length() - 1) != ']'))
				&& ((localStringBuilder.charAt(0) != '{')
						|| (localStringBuilder.charAt(localStringBuilder.length() - 1) != '}')))
			localStringBuilder.insert(0, "[").append("]");
		return localStringBuilder.toString();
	}

	public static String trimSufffix(String toTrim, String trimStr) {
		while (toTrim.endsWith(trimStr))
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		return toTrim;
	}

	public static String getArrayAsString(String[] arr) {
		if ((arr == null) || (arr.length == 0))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			if (i > 0)
				sb.append(",");
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String trimPrefix(String toTrim, String trimStr) {
		while (toTrim.startsWith(trimStr))
			toTrim = toTrim.substring(trimStr.length());
		return toTrim;
	}

	public static String formatParamMsg(String message, Object[] args) {
		for (int i = 0; i < args.length; ++i) {
			message = message.replace(new StringBuilder().append("{").append(i).append("}").toString(),
					args[i].toString());
		}
		return message;
	}

	public static String formatParamMsg(String message, Map params) {
		if (params == null)
			return message;
		Iterator keyIts = params.keySet().iterator();
		while (keyIts.hasNext()) {
			String key = (String) keyIts.next();
			Object val = params.get(key);
			if (val != null)
				message = message.replace(new StringBuilder().append("${").append(key).append("}").toString(),
						val.toString());
		}

		return message;
	}

	/**
	 * 中文转Unicode
	 * 
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		StringBuffer uStr = new StringBuffer();
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			if (iValue <= 256) {
				uStr.append(arChar[i]);
			} else {
				uStr.append("\\u").append(Integer.toHexString(iValue));
			}
		}
		return uStr.toString();
	}

	/**
	 * 将字符串转换成数组格式（主要对应转换前台JSON格式数据）
	 * 
	 * @param str
	 * @return
	 */
	public static List<Map<String, String>> splitToArray(String str) {
		if (str == null || str.trim().length() == 0)
			return null;
		String t = str.replace("，", ",").replace("：", ":").replace("{", "").replace("}", "").replace("|", "");
		t = replaceBlank(t);
		String[] first = t.split(",");
		String[] second;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for (String s : first) {
			second = s.split(":");
			if (second.length == 2) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(second[0], second[1]);
				data.add(map);
			}
		}
		return data;
	}

	/**
	 * 格式化数值类型数据
	 * 
	 * @param num
	 *            数值
	 * @param length
	 *            格式化长度(长度不足位置将以0表示)
	 * @return
	 */
	public static String formatLength(long num, int length) {
		if (num < 0) {
			num = num * -1;
		}
		String snum = String.valueOf(num);
		if (length <= snum.length()) {
			return snum;
		}
		int end = length - snum.length();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < end; i++) {
			buffer.append("0");
		}
		buffer.append(snum);
		return buffer.toString();
	}

	/**
	 * 拼接字符串集合
	 * 
	 * @param collection
	 *            需要拼接的集合
	 * @param joinChar
	 *            拼接字符
	 * @return
	 */
	public static String concatCollection(Collection<String> collection, String joinChar) {
		int size = 0;
		// empty
		if (null == collection || (size = collection.size()) == 0) {
			return "";
		}
		// only one element
		if (size == 1) {
			return collection.iterator().next();
		}
		// multi element
		String r = "";
		StringBuffer result = new StringBuffer();
		Iterator<String> it = collection.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			result.append(joinChar);
		}
		r = result.substring(0, result.toString().trim().length() - 1);
		return r;
	}

	/**
	 * 判断是否包含了某个str字符串 举例：123,22,2233 是否包含22
	 * 
	 * @param source
	 * @param str
	 * @param splitStr
	 *            默认为,
	 * @return
	 */
	public static boolean containStr(String source, String str, String splitStr) {
		if (isNull(splitStr)) {
			splitStr = ",";
		}
		return (splitStr + source + splitStr).indexOf(splitStr + str + splitStr) != -1;
	}

	public static boolean containStr(String source, String str) {
		return containStr(source, str, ",");
	}

	public static void main(String[] args) {
		System.out.println(containStr("123,22,2233", "22"));
	}
}
