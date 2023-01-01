package com.autonomouslogic.dynamomapper.test;

public interface FailTriFunction<A, B, C, R> {
	R apply(A a, B b, C c) throws Exception;
}
