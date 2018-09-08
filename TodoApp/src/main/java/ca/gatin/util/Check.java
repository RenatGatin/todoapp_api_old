package ca.gatin.util;

import java.util.Collection;
import java.util.Map;

public class Check {

	private Check() {
	};

	public static boolean isEmpty(String value) {
		return (value == null || value.length() <= 0);
	}

	public static boolean hasText(String value) {
		return (value != null && value.trim().length() > 0);
	}

	public static boolean isEmpty(Collection<?> coll) {
		return (coll == null || coll.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length <= 0);
	}

	public static boolean isEmpty(Object value) {
		return (value == null || (value instanceof String ? ((String) value).length() <= 0 : false));
	}
}