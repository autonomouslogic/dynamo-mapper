package com.autonomouslogic.dynamomapper.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

public class CheckedFunctionTest {
	@Test
	void shouldExecuteFunction() {
		var ref = new AtomicReference<String>();
		var fun = new CheckedFunction<String, String>() {
			@Override
			public String checkedApply(String s) throws Exception {
				ref.set(s);
				return "return";
			}
		};
		assertEquals("return", fun.apply("input"));
		assertEquals("input", ref.get());
	}

	@Test
	void shouldCheckErrors() {
		var fun = new CheckedFunction<String, String>() {
			@Override
			public String checkedApply(String s) throws Exception {
				throw new Exception("Inner exception");
			}
		};
		var exception = assertThrows(RuntimeException.class, () -> fun.apply("test"));
		assertEquals("java.lang.Exception: Inner exception", exception.getMessage());
		var cause = exception.getCause();
		assertEquals(Exception.class, cause.getClass());
		assertEquals("Inner exception", cause.getMessage());
	}
}
