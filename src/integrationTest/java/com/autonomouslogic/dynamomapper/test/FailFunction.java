package com.autonomouslogic.dynamomapper.test;

public interface FailFunction<A, R> {
	R apply(A a) throws Exception;
}
