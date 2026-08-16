package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.autonomouslogic.dynamomapper.model.TestObject;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import tools.jackson.databind.json.JsonMapper;

public class DynamoMapperBuilderTest {
	static final Map<String, AttributeValue> KEY_MAP =
			Map.of("string", AttributeValue.builder().s("key1").build());
	static final GetItemRequest GET_REQUEST =
			GetItemRequest.builder().tableName("test").key(KEY_MAP).build();

	@Test
	@SneakyThrows
	void mapperShouldUseProvidedClient() {
		var mockClient = mock(DynamoDbClient.class);
		var response = GetItemResponse.builder().item(KEY_MAP).build();
		when(mockClient.getItem(GET_REQUEST)).thenReturn(response);

		var mapper = new DynamoMapperBuilder().client(mockClient).build();
		mapper.getItem(GET_REQUEST, TestObject.class);

		verify(mockClient).getItem(GET_REQUEST);
	}

	@Test
	@SneakyThrows
	void mapperShouldUseProvidedJsonMapper() {
		var mockClient = mock(DynamoDbClient.class);
		var response = GetItemResponse.builder().item(KEY_MAP).build();
		when(mockClient.getItem(any(GetItemRequest.class))).thenReturn(response);
		var customMapper = JsonMapper.builder().build();

		var mapper = new DynamoMapperBuilder()
				.client(mockClient)
				.jsonMapper(customMapper)
				.build();
		var result = mapper.getItem(GET_REQUEST, TestObject.class);

		assertNotNull(result);
		assertEquals("key1", result.item().string());
	}

	@Test
	void mapperShouldApplyTableNameDecorator() {
		var mapper = new DynamoMapperBuilder()
				.client(mock(DynamoDbClient.class))
				.tableNameDecorator((clazz, name) -> "prefix-" + name)
				.build();

		assertEquals("prefix-test", mapper.reflectionUtil.resolveTableName(TestObject.class));
	}
}
