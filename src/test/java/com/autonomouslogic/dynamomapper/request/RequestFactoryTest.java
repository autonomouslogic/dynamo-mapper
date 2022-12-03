package com.autonomouslogic.dynamomapper.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

public class RequestFactoryTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	RequestFactory factory =
			new RequestFactory(new DynamoEncoder(objectMapper), objectMapper, new ReflectionUtil(objectMapper));

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromPrimaryKey() {
		var request =
				factory.getItemRequestFromPrimaryKey("key1", TestObject.class).build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromKeyObject() {
		var request = factory.getItemRequestFromKeyObject(new TestObject().setString("key1"))
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}

	@Test
	@SneakyThrows
	void shouldCreateBatchGetItemRequestFromPrimaryKey() {
		var request = factory.batchGetItemRequestFromPrimaryKeys(List.of("key1", "key2"), TestObject.class)
				.build();
		assertEquals(Set.of("test"), request.requestItems().keySet());
		var keys = request.requestItems().get("test").keys();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), keys.get(0));
		assertEquals(Map.of("string", AttributeValue.builder().s("key2").build()), keys.get(1));
	}

	@Test
	@SneakyThrows
	void shouldCreateBatchGetItemRequestFromKeyObject() {
		var request = factory.batchGetItemRequestFromKeyObjects(
						List.of(new TestObject().setString("key1"), new TestObject().setString("key2")))
				.build();
		assertEquals(Set.of("test"), request.requestItems().keySet());
		var keys = request.requestItems().get("test").keys();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), keys.get(0));
		assertEquals(Map.of("string", AttributeValue.builder().s("key2").build()), keys.get(1));
	}

	@Test
	@SneakyThrows
	void shouldCreatePutItemRequestFromKeyObject() {
		var request = factory.putItemRequestFromKeyObject(
						new TestObject().setString("key1").setNumber(27))
				.build();
		var expected = Map.ofEntries(
				Map.entry("string", AttributeValue.builder().s("key1").build()),
				Map.entry("number", AttributeValue.builder().n("27").build()));
		assertEquals(expected, request.item());
	}

	@Test
	@SneakyThrows
	void shouldCreateUpdateItemRequestFromKeyObject() {
		var request = factory.updateItemRequestFromKeyObject(
						new TestObject().setString("key1").setNumber(27))
				.build();
		var expected = Map.of("string", AttributeValue.builder().s("key1").build());
		var updates = Map.of(
				"number",
				AttributeValueUpdate.builder()
						.value(AttributeValue.builder().n("27").build())
						.action(AttributeAction.PUT)
						.build());
		assertEquals(expected, request.key());
		assertEquals(updates, request.attributeUpdates());
	}

	@Test
	@SneakyThrows
	void shouldCreateDeleteItemRequestFromPrimaryKey() {
		var request = factory.deleteItemRequestFromPrimaryKey("key1", TestObject.class)
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}

	@Test
	@SneakyThrows
	void shouldCreateDeleteItemRequestFromKeyObject() {
		var request = factory.deleteItemRequestFromKeyObject(new TestObject().setString("key1"))
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
	}
}
