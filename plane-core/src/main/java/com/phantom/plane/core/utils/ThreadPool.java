package com.phantom.plane.core.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadPool {

	private static ThreadLocal pool = new ThreadLocal() {
		@Override
		protected Object initialValue() {
			return new HashMap();
		}
	};

	public static Object get(String key) {
		Map map = (Map) pool.get();
		return map.get(key);
	}

	public static void add(String key, Object value) {
		Map map = (Map) pool.get();
		map.put(key, value);
		pool.set(map);
	}

	public static Map getMap() {
		return (Map) pool.get();
	}

	public static void clear() {
		pool.remove();
		pool.set(new HashMap());
	}
}
