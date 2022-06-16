package com.autonomouslogic.dynamomapper.util;

import com.autonomouslogic.dynamomapper.model.MethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedMethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedTestObject;
import com.autonomouslogic.dynamomapper.model.TestObject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionUtilTest {
	ReflectionUtil reflectionUtil = new ReflectionUtil(StdObjectMapper.objectMapper());

	@Test
	@SneakyThrows
	void shouldResolveHashKeyField() {
		assertEquals(
			List.of("string"),
			reflectionUtil.resolveHashKeyFields(TestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolveHashKeyFieldFromModifiedJsonProperty() {
		assertEquals(
			List.of("hash_key"),
			reflectionUtil.resolveHashKeyFields(ModifiedTestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolveHashKeyMethod() {
		assertEquals(
			List.of("hashKey"),
			reflectionUtil.resolveHashKeyFields(MethodTestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolveHashKeyMethodFromModifiedJsonProperty() {
		assertEquals(
			List.of("hash_key"),
			reflectionUtil.resolveHashKeyFields(ModifiedMethodTestObject.class));
	}
}
