/**
 * Project:				easyweb-core
 * Author:				Green
 * Company: 			杭州中软
 * Created Date:		2014-9-28
 * Description:			请填写该类的功能描述
 * Copyright @ 2014 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 无重复值无空值map
 * @author Green
 *
 * @param <V>
 */
public class NotNullLinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V>{
	private final Map<String, String> caseInsensitiveKeys;

	private final Locale locale;

	public NotNullLinkedCaseInsensitiveMap() {
		this(null);
	}

	public NotNullLinkedCaseInsensitiveMap(Locale locale) {
		super();
		this.caseInsensitiveKeys = new HashMap<String, String>();
		this.locale = (locale != null ? locale : Locale.getDefault());
	}

	public NotNullLinkedCaseInsensitiveMap(int initialCapacity) {
		this(initialCapacity, null);
	}

	public NotNullLinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
		super(initialCapacity);
		this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
		this.locale = (locale != null ? locale : Locale.getDefault());
	}


	@Override
	public V put(String key, V value) {
		if(value == null) return null;
		this.caseInsensitiveKeys.put(convertKey(key), key);
		return super.put(key, value);
	}

	@Override
	public boolean containsKey(Object key) {
		return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String) key)));
	}

	@Override
	public V get(Object key) {
		if (key instanceof String) {
			return super.get(this.caseInsensitiveKeys.get(convertKey((String) key)));
		}
		else {
			return null;
		}
	}

	@Override
	public V remove(Object key) {
		if (key instanceof String ) {
			return super.remove(this.caseInsensitiveKeys.remove(convertKey((String) key)));
		}
		else {
			return null;
		}
	}

	@Override
	public void clear() {
		this.caseInsensitiveKeys.clear();
		super.clear();
	}

	protected String convertKey(String key) {
		return key.toLowerCase(this.locale);
	}


}
