package com.autonomouslogic.dynamomapper.function;


import java.util.function.Consumer;

public class NoopConsumer<T> implements Consumer<T> {
	private static final NoopConsumer NOOP = new NoopConsumer();

	@Override
	public void accept(T o) {
		// Noop.
	}

	@SuppressWarnings("unchecked")
	public static <T> NoopConsumer<T> noop() {
		return (NoopConsumer<T>) NOOP;
	}
}
