package com.autonomouslogic.dynamomapper.function;

public interface ThrowingFutureSupplier<T> {
	T get() throws Exception;
}
