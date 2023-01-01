package com.autonomouslogic.dynamomapper.test;

public interface FailBiFunction<A, B, R> {
	R apply(A a, B b) throws Exception;
}
