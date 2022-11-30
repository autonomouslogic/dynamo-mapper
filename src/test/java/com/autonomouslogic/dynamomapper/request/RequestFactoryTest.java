package com.autonomouslogic.dynamomapper.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class RequestFactoryTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	RequestFactory factory =
			new RequestFactory(new DynamoEncoder(objectMapper), objectMapper, new ReflectionUtil(objectMapper));

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromPrimaryKey() {
		var request = factory.getRequestFromPrimaryKey("key1", TestObject.class).build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromKeyObject() {
		var request = factory.getRequestFromKeyObject(new TestObject().setString("key1"))
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}
}
