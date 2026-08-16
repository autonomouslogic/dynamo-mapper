package com.autonomouslogic.dynamomapper.util;

import java.util.List;

public class ListUtil {
	@SuppressWarnings("unchecked")
	public static <T> Class<T> elementClass(List<?> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Cannot determine element class from null or empty list");
		}
		return (Class<T>) list.get(0).getClass();
	}
}
