package com.autonomouslogic.dynamomapper.reflect;

import com.autonomouslogic.dynamomapper.test.MethodTestObject;
import com.autonomouslogic.dynamomapper.test.ModifiedMethodTestObject;
import com.autonomouslogic.dynamomapper.test.ModifiedTestObject;
import com.autonomouslogic.dynamomapper.test.StdObjectMapper;
import com.autonomouslogic.dynamomapper.test.TestObject;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionUtilTest {
	ReflectionUtil reflectionUtil = new ReflectionUtil(StdObjectMapper.objectMapper());

	@Test
	@SneakyThrows
	public void shouldResolveHashKeyField() {
		assertEquals(
			List.of("string"),
			reflectionUtil.resolveHashKeyFields(TestObject.class));
	}

	@Test
	@SneakyThrows
	public void shouldResolveHashKeyFieldFromModifiedJsonProperty() {
		assertEquals(
			List.of("hash_key"),
			reflectionUtil.resolveHashKeyFields(ModifiedTestObject.class));
	}

	@Test
	@SneakyThrows
	public void shouldResolveHashKeyMethod() {
		assertEquals(
			List.of("hashKey"),
			reflectionUtil.resolveHashKeyFields(MethodTestObject.class));
	}

	@Test
	@SneakyThrows
	public void shouldResolveHashKeyMethodFromModifiedJsonProperty() {
		assertEquals(
			List.of("hash_key"),
			reflectionUtil.resolveHashKeyFields(ModifiedMethodTestObject.class));
	}
}
