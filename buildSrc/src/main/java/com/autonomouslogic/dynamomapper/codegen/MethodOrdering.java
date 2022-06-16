package com.autonomouslogic.dynamomapper.codegen;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodOrdering implements Comparator<Method> {
	private static final Comparator<Method> name = Comparator.comparing(Method::getName);
	private static final Comparator<Method> paramCount = Comparator.comparingInt(Method::getParameterCount);
	private static final Comparator<Method> paramTypes = Comparator.comparing(m -> Stream.of(m.getParameters())
		.map(p -> p.getType().getSimpleName())
		.collect(Collectors.joining("-")));

	private static final Comparator<Method> delegate = name
		.thenComparing(paramCount)
		.thenComparing(paramTypes);

	@Override
	public int compare(Method m1, Method m2) {
		return delegate.compare(m1, m2);
	}
}
