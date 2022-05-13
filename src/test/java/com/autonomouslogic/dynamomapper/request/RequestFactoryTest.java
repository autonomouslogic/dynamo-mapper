package com.autonomouslogic.dynamomapper.request;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.test.StdObjectMapper;
import com.autonomouslogic.dynamomapper.model.TestObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestFactoryTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	RequestFactory factory = new RequestFactory(
		new DynamoEncoder(objectMapper),
		objectMapper,
		new ReflectionUtil(objectMapper)
	);

	@Test
	@SneakyThrows
	public void shouldCreateGetItemRequestsFromHashKey() {
		var request = factory.getRequestFromHashKey("key1", TestObject.class).build();
		assertEquals(
			Map.of("string", AttributeValue.builder().s("key1").build()),
			request.key());
	}

	@Test
	@SneakyThrows
	public void shouldCreateGetItemRequestsFromKeyObjects() {
		var request = factory.getRequestFromKeyObject(new TestObject().setString("key1")).build();
		assertEquals(
			Map.of("string", AttributeValue.builder().s("key1").build()),
			request.key());
	}
}
