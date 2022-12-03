package com.autonomouslogic.dynamomapper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.model.MethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedMethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedTestObject;
import com.autonomouslogic.dynamomapper.model.TestObject;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ReflectionUtilTest {
	ReflectionUtil reflectionUtil = new ReflectionUtil(StdObjectMapper.objectMapper());

	@Test
	@SneakyThrows
	void shouldResolvePrimaryKeyField() {
		assertEquals(List.of("string"), reflectionUtil.resolvePrimaryKeyFields(TestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolvePrimaryKeyFieldFromModifiedJsonProperty() {
		assertEquals(List.of("primary_key"), reflectionUtil.resolvePrimaryKeyFields(ModifiedTestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolvePrimaryKeyMethod() {
		assertEquals(List.of("primaryKey"), reflectionUtil.resolvePrimaryKeyFields(MethodTestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldResolvePrimaryKeyMethodFromModifiedJsonProperty() {
		assertEquals(List.of("primary_key"), reflectionUtil.resolvePrimaryKeyFields(ModifiedMethodTestObject.class));
	}
}
