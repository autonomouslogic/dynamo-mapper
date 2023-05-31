package com.autonomouslogic.dynamomapper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.model.MethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedMethodTestObject;
import com.autonomouslogic.dynamomapper.model.ModifiedTestObject;
import com.autonomouslogic.dynamomapper.model.TestObject;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class ReflectionUtilTest {
	ReflectionUtil reflectionUtil = new ReflectionUtil(StdObjectMapper.objectMapper(), null);

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

	@Test
	@SneakyThrows
	void shouldResolveTableName() {
		assertEquals("test", reflectionUtil.resolveTableName(TestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldDecorateTableName() {
		TableNameDecorator decorator = (clazz, tableName) -> tableName + "-" + clazz.getSimpleName();
		var reflectionUtil = new ReflectionUtil(StdObjectMapper.objectMapper(), decorator);
		assertEquals("test-TestObject", reflectionUtil.resolveTableName(TestObject.class));
	}
}
