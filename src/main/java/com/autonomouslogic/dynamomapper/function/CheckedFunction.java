package com.autonomouslogic.dynamomapper.function;

import java.util.function.Function;

public abstract class CheckedFunction<T, R> implements Function<T, R> {
	@Override
	public final R apply(T t) {
		try {
			return checkedApply(t);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract R checkedApply(T t) throws Exception;
}
