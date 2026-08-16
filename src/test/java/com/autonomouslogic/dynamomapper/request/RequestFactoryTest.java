package com.autonomouslogic.dynamomapper.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdJsonMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import tools.jackson.databind.json.JsonMapper;

public class RequestFactoryTest {
	JsonMapper jsonMapper = StdJsonMapper.jsonMapper();
	TableNameDecorator decorator;
	ReflectionUtil reflectionUtil;
	DynamoEncoder dynamoEncoder;
	RequestFactory factory;

	@BeforeEach
	void setUp() {
		decorator = Mockito.mock(TableNameDecorator.class);
		when(decorator.apply(any(), any()))
				.thenAnswer((Answer<String>) invocation -> invocation.getArgument(1, String.class));

		reflectionUtil = new ReflectionUtil(jsonMapper, decorator);
		dynamoEncoder = new DynamoEncoder(jsonMapper, reflectionUtil);
		factory = new RequestFactory(dynamoEncoder, jsonMapper, reflectionUtil);
	}

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromPrimaryKey() {
		var request =
				factory.getItemRequestFromPrimaryKey("key1", TestObject.class).build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreateGetItemRequestFromKeyObject() {
		var request = factory.getItemRequestFromKeyObject(
						TestObject.builder().string("key1").build())
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
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

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreateBatchGetItemRequestFromKeyObject() {
		var request = factory.batchGetItemRequestFromKeyObjects(List.of(
						TestObject.builder().string("key1").build(),
						TestObject.builder().string("key2").build()))
				.build();
		assertEquals(Set.of("test"), request.requestItems().keySet());
		var keys = request.requestItems().get("test").keys();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), keys.get(0));
		assertEquals(Map.of("string", AttributeValue.builder().s("key2").build()), keys.get(1));

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreatePutItemRequestFromKeyObject() {
		var request = factory.putItemRequestFromKeyObject(
						TestObject.builder().string("key1").number(27).build())
				.build();
		var expected = Map.ofEntries(
				Map.entry("string", AttributeValue.builder().s("key1").build()),
				Map.entry("number", AttributeValue.builder().n("27").build()));
		assertEquals(expected, request.item());
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreateUpdateItemRequestFromKeyObject() {
		var request = factory.updateItemRequestFromKeyObject(
						TestObject.builder().string("key1").number(27).build())
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
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreateDeleteItemRequestFromPrimaryKey() {
		var request = factory.deleteItemRequestFromPrimaryKey("key1", TestObject.class)
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldCreateDeleteItemRequestFromKeyObject() {
		var request = factory.deleteItemRequestFromKeyObject(
						TestObject.builder().string("key1").build())
				.build();
		assertEquals(Map.of("string", AttributeValue.builder().s("key1").build()), request.key());
		assertEquals("test", request.tableName());

		verify(decorator).apply(TestObject.class, "test");
	}

	@Test
	@SneakyThrows
	void shouldRejectBatchGetItemRequestFromKeyObjectsWithMixedClasses() {
		var objects = List.of(TestObject.builder().string("key1").build(), "not-a-test-object");
		assertThrows(IllegalArgumentException.class, () -> factory.batchGetItemRequestFromKeyObjects(objects));
	}

	@Test
	void shouldRejectNullPrimaryKeyInGetItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.getItemRequestFromPrimaryKey(null, TestObject.class));
	}

	@Test
	void shouldRejectNullClassInGetItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.getItemRequestFromPrimaryKey("key", null));
	}

	@Test
	void shouldRejectNullKeyObjectInGetItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.getItemRequestFromKeyObject(null));
	}

	@Test
	void shouldRejectNullObjectInPutItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.putItemRequestFromKeyObject(null));
	}

	@Test
	void shouldRejectNullObjectInDeleteItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.deleteItemRequestFromKeyObject(null));
	}

	@Test
	void shouldRejectNullPrimaryKeyListInBatchGetItemRequest() {
		assertThrows(
				NullPointerException.class, () -> factory.batchGetItemRequestFromPrimaryKeys(null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptGetItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptGetItemRequest((GetItemRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullClassInAcceptGetItemRequest() {
		var req = GetItemRequest.builder()
				.tableName("test")
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		assertThrows(NullPointerException.class, () -> factory.acceptGetItemRequest(req, null));
	}

	@Test
	void shouldRejectNullRequestInAcceptPutItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptPutItemRequest((PutItemRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptScanRequest() {
		assertThrows(NullPointerException.class, () -> factory.acceptScanRequest((ScanRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptQueryRequest() {
		assertThrows(
				NullPointerException.class, () -> factory.acceptQueryRequest((QueryRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptUpdateItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptUpdateItemRequest((UpdateItemRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptDeleteItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptDeleteItemRequest((DeleteItemRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullRequestInAcceptBatchGetItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptBatchGetItemRequest((BatchGetItemRequest) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptGetItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptGetItemRequest((GetItemRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptPutItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptPutItemRequest((PutItemRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptUpdateItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptUpdateItemRequest((UpdateItemRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptDeleteItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptDeleteItemRequest((DeleteItemRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptScanRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptScanRequest((ScanRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptQueryRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptQueryRequest((QueryRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullBuilderInAcceptBatchGetItemRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptBatchGetItemRequest((BatchGetItemRequest.Builder) null, TestObject.class));
	}

	@Test
	void shouldRejectNullClassInAcceptGetItemRequestBuilder() {
		assertThrows(NullPointerException.class, () -> factory.acceptGetItemRequest(GetItemRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptPutItemRequest() {
		var req = PutItemRequest.builder().tableName("t").item(Map.of()).build();
		assertThrows(NullPointerException.class, () -> factory.acceptPutItemRequest(req, null));
	}

	@Test
	void shouldRejectNullClassInAcceptPutItemRequestBuilder() {
		assertThrows(NullPointerException.class, () -> factory.acceptPutItemRequest(PutItemRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptUpdateItemRequest() {
		var req = UpdateItemRequest.builder().tableName("t").key(Map.of()).build();
		assertThrows(NullPointerException.class, () -> factory.acceptUpdateItemRequest(req, null));
	}

	@Test
	void shouldRejectNullClassInAcceptUpdateItemRequestBuilder() {
		assertThrows(
				NullPointerException.class, () -> factory.acceptUpdateItemRequest(UpdateItemRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptDeleteItemRequest() {
		var req = DeleteItemRequest.builder().tableName("t").key(Map.of()).build();
		assertThrows(NullPointerException.class, () -> factory.acceptDeleteItemRequest(req, null));
	}

	@Test
	void shouldRejectNullClassInAcceptDeleteItemRequestBuilder() {
		assertThrows(
				NullPointerException.class, () -> factory.acceptDeleteItemRequest(DeleteItemRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptBatchGetItemRequest() {
		var req = BatchGetItemRequest.builder()
				.requestItems(Map.of("t", KeysAndAttributes.builder().build()))
				.build();
		assertThrows(NullPointerException.class, () -> factory.acceptBatchGetItemRequest(req, null));
	}

	@Test
	void shouldRejectNullClassInAcceptBatchGetItemRequestBuilder() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptBatchGetItemRequest(BatchGetItemRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptScanRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptScanRequest(ScanRequest.builder().build(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptScanRequestBuilder() {
		assertThrows(NullPointerException.class, () -> factory.acceptScanRequest(ScanRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptQueryRequest() {
		assertThrows(
				NullPointerException.class,
				() -> factory.acceptQueryRequest(QueryRequest.builder().build(), null));
	}

	@Test
	void shouldRejectNullClassInAcceptQueryRequestBuilder() {
		assertThrows(NullPointerException.class, () -> factory.acceptQueryRequest(QueryRequest.builder(), null));
	}

	@Test
	void shouldRejectNullClassInBatchGetItemRequestFromPrimaryKeys() {
		assertThrows(
				NullPointerException.class, () -> factory.batchGetItemRequestFromPrimaryKeys(List.of("key"), null));
	}

	@Test
	void shouldRejectNullClassInDeleteItemRequestFromPrimaryKey() {
		assertThrows(NullPointerException.class, () -> factory.deleteItemRequestFromPrimaryKey("key", null));
	}

	@Test
	void shouldRejectNullObjectInUpdateItemRequest() {
		assertThrows(NullPointerException.class, () -> factory.updateItemRequestFromKeyObject(null));
	}

	@Test
	@SneakyThrows
	void shouldAcceptGetItemRequestWithTableName() {
		var req = GetItemRequest.builder()
				.tableName("explicit-table")
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptGetItemRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptGetItemRequestWithoutTableName() {
		var req = GetItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptGetItemRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptGetItemRequestBuilderSetsTableName() {
		var builder = GetItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()));
		var result = factory.acceptGetItemRequest(builder, TestObject.class).build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptBatchGetItemRequestWithSingleClass() {
		var req = BatchGetItemRequest.builder()
				.requestItems(Map.of(
						"test",
						KeysAndAttributes.builder()
								.keys(Map.of(
										"string",
										AttributeValue.builder().s("k").build()))
								.build()))
				.build();
		var result = factory.acceptBatchGetItemRequest(req, TestObject.class);
		assertEquals(Set.of("test"), result.requestItems().keySet());
	}

	@Test
	@SneakyThrows
	void shouldRejectBatchGetItemRequestWithMultipleTableKeys() {
		var req = BatchGetItemRequest.builder()
				.requestItems(Map.of(
						"table1",
						KeysAndAttributes.builder()
								.keys(Map.of(
										"string",
										AttributeValue.builder().s("a").build()))
								.build(),
						"table2",
						KeysAndAttributes.builder()
								.keys(Map.of(
										"string",
										AttributeValue.builder().s("b").build()))
								.build()))
				.build();
		assertThrows(IllegalArgumentException.class, () -> factory.acceptBatchGetItemRequest(req, TestObject.class));
	}

	@Test
	@SneakyThrows
	void shouldAcceptPutItemRequestWithTableName() {
		var req = PutItemRequest.builder()
				.tableName("explicit-table")
				.item(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptPutItemRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptPutItemRequestWithoutTableName() {
		var req = PutItemRequest.builder()
				.item(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptPutItemRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptPutItemRequestBuilderSetsTableName() {
		var builder = PutItemRequest.builder()
				.item(Map.of("string", AttributeValue.builder().s("k").build()));
		var result = factory.acceptPutItemRequest(builder, TestObject.class).build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptUpdateItemRequestWithTableName() {
		var req = UpdateItemRequest.builder()
				.tableName("explicit-table")
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptUpdateItemRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptUpdateItemRequestWithoutTableName() {
		var req = UpdateItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptUpdateItemRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptUpdateItemRequestBuilderSetsTableName() {
		var builder = UpdateItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()));
		var result = factory.acceptUpdateItemRequest(builder, TestObject.class).build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptDeleteItemRequestWithTableName() {
		var req = DeleteItemRequest.builder()
				.tableName("explicit-table")
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptDeleteItemRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptDeleteItemRequestWithoutTableName() {
		var req = DeleteItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()))
				.build();
		var result = factory.acceptDeleteItemRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptDeleteItemRequestBuilderSetsTableName() {
		var builder = DeleteItemRequest.builder()
				.key(Map.of("string", AttributeValue.builder().s("k").build()));
		var result = factory.acceptDeleteItemRequest(builder, TestObject.class).build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptScanRequestWithTableName() {
		var req = ScanRequest.builder().tableName("explicit-table").build();
		var result = factory.acceptScanRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptScanRequestWithoutTableName() {
		var req = ScanRequest.builder().build();
		var result = factory.acceptScanRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptScanRequestBuilderSetsTableName() {
		var result = factory.acceptScanRequest(ScanRequest.builder(), TestObject.class)
				.build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptQueryRequestWithTableName() {
		var req = QueryRequest.builder().tableName("explicit-table").build();
		var result = factory.acceptQueryRequest(req, TestObject.class);
		assertEquals("explicit-table", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptQueryRequestWithoutTableName() {
		var req = QueryRequest.builder().build();
		var result = factory.acceptQueryRequest(req, TestObject.class);
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldAcceptQueryRequestBuilderSetsTableName() {
		var result = factory.acceptQueryRequest(QueryRequest.builder(), TestObject.class)
				.build();
		assertEquals("test", result.tableName());
	}

	@Test
	@SneakyThrows
	void shouldRejectBatchGetItemRequestFromKeyObjectsWithEmptyList() {
		assertThrows(IllegalArgumentException.class, () -> factory.batchGetItemRequestFromKeyObjects(List.of()));
	}

	@Test
	@SneakyThrows
	void shouldAcceptBatchGetItemRequestBuilder() {
		var builder = BatchGetItemRequest.builder()
				.requestItems(Map.of(
						"test",
						KeysAndAttributes.builder()
								.keys(Map.of(
										"string",
										AttributeValue.builder().s("k").build()))
								.build()));
		var result =
				factory.acceptBatchGetItemRequest(builder, TestObject.class).build();
		assertEquals(Set.of("test"), result.requestItems().keySet());
	}
}
