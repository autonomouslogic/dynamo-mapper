package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.autonomouslogic.dynamomapper.model.TestObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

/**
 * Exercises the generated {@link DynamoAsyncMapper} delegate methods directly, since they're
 * otherwise only covered by the real-AWS integration tests.
 */
public class DynamoAsyncMapperTest {
	static final Map<String, AttributeValue> KEY_MAP =
			Map.of("string", AttributeValue.builder().s("key1").build());
	static final TestObject KEY_OBJECT = TestObject.builder().string("key1").build();

	DynamoDbAsyncClient mockClient = mock(DynamoDbAsyncClient.class);
	DynamoAsyncMapper mapper = new DynamoAsyncMapperBuilder().client(mockClient).build();

	// getItem

	@Test
	@SneakyThrows
	void shouldGetItemFromRequest() {
		when(mockClient.getItem(any(GetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						GetItemResponse.builder().build()));
		var request = GetItemRequest.builder().key(KEY_MAP).build();

		mapper.getItem(request, TestObject.class).join();

		verify(mockClient)
				.getItem(GetItemRequest.builder().key(KEY_MAP).tableName("test").build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldGetItemFromConsumer() {
		when(mockClient.getItem(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<GetItemRequest.Builder> consumer = inv.getArgument(0);
			var builder = GetItemRequest.builder().key(KEY_MAP);
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(GetItemResponse.builder().build());
		});

		mapper.getItem(b -> {}, TestObject.class).join();

		verify(mockClient).getItem(any(Consumer.class));
	}

	@Test
	@SneakyThrows
	void shouldGetItemFromPrimaryKey() {
		when(mockClient.getItem(any(GetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						GetItemResponse.builder().build()));

		mapper.getItemFromPrimaryKey("key1", TestObject.class).join();

		verify(mockClient)
				.getItem(GetItemRequest.builder().tableName("test").key(KEY_MAP).build());
	}

	@Test
	@SneakyThrows
	void shouldGetItemFromPrimaryKeyWithConsumer() {
		when(mockClient.getItem(any(GetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						GetItemResponse.builder().build()));

		mapper.getItemFromPrimaryKey("key1", b -> b.consistentRead(true), TestObject.class)
				.join();

		verify(mockClient)
				.getItem(GetItemRequest.builder()
						.tableName("test")
						.key(KEY_MAP)
						.consistentRead(true)
						.build());
	}

	@Test
	@SneakyThrows
	void shouldGetItemFromKeyObject() {
		when(mockClient.getItem(any(GetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						GetItemResponse.builder().build()));

		mapper.getItemFromKeyObject(KEY_OBJECT).join();

		verify(mockClient)
				.getItem(GetItemRequest.builder().tableName("test").key(KEY_MAP).build());
	}

	@Test
	@SneakyThrows
	void shouldGetItemFromKeyObjectWithConsumer() {
		when(mockClient.getItem(any(GetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						GetItemResponse.builder().build()));

		mapper.getItemFromKeyObject(KEY_OBJECT, b -> b.consistentRead(true)).join();

		verify(mockClient)
				.getItem(GetItemRequest.builder()
						.tableName("test")
						.key(KEY_MAP)
						.consistentRead(true)
						.build());
	}

	// batchGetItem

	@Test
	@SneakyThrows
	void shouldBatchGetItemFromRequest() {
		when(mockClient.batchGetItem(any(BatchGetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						BatchGetItemResponse.builder().build()));
		var request = BatchGetItemRequest.builder()
				.requestItems(
						Map.of("test", KeysAndAttributes.builder().keys(KEY_MAP).build()))
				.build();

		mapper.batchGetItem(request, TestObject.class).join();

		verify(mockClient).batchGetItem(request);
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldBatchGetItemFromConsumer() {
		when(mockClient.batchGetItem(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<BatchGetItemRequest.Builder> consumer = inv.getArgument(0);
			var builder = BatchGetItemRequest.builder()
					.requestItems(Map.of(
							"test", KeysAndAttributes.builder().keys(KEY_MAP).build()));
			consumer.accept(builder);
			return CompletableFuture.completedFuture(
					BatchGetItemResponse.builder().build());
		});

		mapper.batchGetItem(b -> {}, TestObject.class).join();

		verify(mockClient).batchGetItem(any(Consumer.class));
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemFromPrimaryKeys() {
		when(mockClient.batchGetItem(any(BatchGetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						BatchGetItemResponse.builder().build()));

		mapper.batchGetItemFromPrimaryKeys(List.of("key1"), TestObject.class).join();

		verify(mockClient)
				.batchGetItem(BatchGetItemRequest.builder()
						.requestItems(Map.of(
								"test",
								KeysAndAttributes.builder().keys(KEY_MAP).build()))
						.build());
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemFromPrimaryKeysWithConsumer() {
		when(mockClient.batchGetItem(any(BatchGetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						BatchGetItemResponse.builder().build()));

		mapper.batchGetItemFromPrimaryKeys(
						List.of("key1"), b -> b.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL), TestObject.class)
				.join();

		verify(mockClient)
				.batchGetItem(BatchGetItemRequest.builder()
						.requestItems(Map.of(
								"test",
								KeysAndAttributes.builder().keys(KEY_MAP).build()))
						.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
						.build());
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemFromKeyObjects() {
		when(mockClient.batchGetItem(any(BatchGetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						BatchGetItemResponse.builder().build()));

		mapper.batchGetItemFromKeyObjects(List.of(KEY_OBJECT), TestObject.class).join();

		verify(mockClient)
				.batchGetItem(BatchGetItemRequest.builder()
						.requestItems(Map.of(
								"test",
								KeysAndAttributes.builder().keys(KEY_MAP).build()))
						.build());
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemFromKeyObjectsWithConsumer() {
		when(mockClient.batchGetItem(any(BatchGetItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						BatchGetItemResponse.builder().build()));

		mapper.batchGetItemFromKeyObjects(
						List.of(KEY_OBJECT),
						TestObject.class,
						b -> b.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL))
				.join();

		verify(mockClient)
				.batchGetItem(BatchGetItemRequest.builder()
						.requestItems(Map.of(
								"test",
								KeysAndAttributes.builder().keys(KEY_MAP).build()))
						.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
						.build());
	}

	// putItem

	@Test
	@SneakyThrows
	void shouldPutItemFromRequest() {
		when(mockClient.putItem(any(PutItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						PutItemResponse.builder().build()));
		var request = PutItemRequest.builder().item(KEY_MAP).build();

		mapper.putItem(request, TestObject.class).join();

		verify(mockClient)
				.putItem(
						PutItemRequest.builder().item(KEY_MAP).tableName("test").build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldPutItemFromConsumer() {
		when(mockClient.putItem(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<PutItemRequest.Builder> consumer = inv.getArgument(0);
			var builder = PutItemRequest.builder().item(KEY_MAP);
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(PutItemResponse.builder().build());
		});

		mapper.putItem(b -> {}, TestObject.class).join();

		verify(mockClient).putItem(any(Consumer.class));
	}

	@Test
	@SneakyThrows
	void shouldPutItemFromKeyObject() {
		when(mockClient.putItem(any(PutItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						PutItemResponse.builder().build()));

		mapper.putItemFromKeyObject(KEY_OBJECT).join();

		verify(mockClient).putItem(any(PutItemRequest.class));
	}

	@Test
	@SneakyThrows
	void shouldPutItemFromKeyObjectWithConsumer() {
		when(mockClient.putItem(any(PutItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						PutItemResponse.builder().build()));

		mapper.putItemFromKeyObject(KEY_OBJECT, b -> b.returnValues("ALL_OLD")).join();

		verify(mockClient).putItem(any(PutItemRequest.class));
	}

	// updateItem

	@Test
	@SneakyThrows
	void shouldUpdateItemFromRequest() {
		when(mockClient.updateItem(any(UpdateItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						UpdateItemResponse.builder().build()));
		var request = UpdateItemRequest.builder().key(KEY_MAP).build();

		mapper.updateItem(request, TestObject.class).join();

		verify(mockClient)
				.updateItem(UpdateItemRequest.builder()
						.key(KEY_MAP)
						.tableName("test")
						.build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldUpdateItemFromConsumer() {
		when(mockClient.updateItem(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<UpdateItemRequest.Builder> consumer = inv.getArgument(0);
			var builder = UpdateItemRequest.builder().key(KEY_MAP);
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(
					UpdateItemResponse.builder().build());
		});

		mapper.updateItem(b -> {}, TestObject.class).join();

		verify(mockClient).updateItem(any(Consumer.class));
	}

	@Test
	@SneakyThrows
	void shouldUpdateItemFromKeyObject() {
		when(mockClient.updateItem(any(UpdateItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						UpdateItemResponse.builder().build()));

		mapper.updateItemFromKeyObject(KEY_OBJECT).join();

		verify(mockClient).updateItem(any(UpdateItemRequest.class));
	}

	@Test
	@SneakyThrows
	void shouldUpdateItemFromKeyObjectWithConsumer() {
		when(mockClient.updateItem(any(UpdateItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						UpdateItemResponse.builder().build()));

		mapper.updateItemFromKeyObject(KEY_OBJECT, b -> b.returnValues("ALL_NEW"))
				.join();

		verify(mockClient).updateItem(any(UpdateItemRequest.class));
	}

	// deleteItem

	@Test
	@SneakyThrows
	void shouldDeleteItemFromRequest() {
		when(mockClient.deleteItem(any(DeleteItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						DeleteItemResponse.builder().build()));
		var request = DeleteItemRequest.builder().key(KEY_MAP).build();

		mapper.deleteItem(request, TestObject.class).join();

		verify(mockClient)
				.deleteItem(DeleteItemRequest.builder()
						.key(KEY_MAP)
						.tableName("test")
						.build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldDeleteItemFromConsumer() {
		when(mockClient.deleteItem(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<DeleteItemRequest.Builder> consumer = inv.getArgument(0);
			var builder = DeleteItemRequest.builder().key(KEY_MAP);
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(
					DeleteItemResponse.builder().build());
		});

		mapper.deleteItem(b -> {}, TestObject.class).join();

		verify(mockClient).deleteItem(any(Consumer.class));
	}

	@Test
	@SneakyThrows
	void shouldDeleteItemFromPrimaryKey() {
		when(mockClient.deleteItem(any(DeleteItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						DeleteItemResponse.builder().build()));

		mapper.deleteItemFromPrimaryKey("key1", TestObject.class).join();

		verify(mockClient)
				.deleteItem(DeleteItemRequest.builder()
						.tableName("test")
						.key(KEY_MAP)
						.build());
	}

	@Test
	@SneakyThrows
	void shouldDeleteItemFromPrimaryKeyWithConsumer() {
		when(mockClient.deleteItem(any(DeleteItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						DeleteItemResponse.builder().build()));

		mapper.deleteItemFromPrimaryKey("key1", b -> b.returnValues("ALL_OLD"), TestObject.class)
				.join();

		verify(mockClient).deleteItem(any(DeleteItemRequest.class));
	}

	@Test
	@SneakyThrows
	void shouldDeleteItemFromKeyObject() {
		when(mockClient.deleteItem(any(DeleteItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						DeleteItemResponse.builder().build()));

		mapper.deleteItemFromKeyObject(KEY_OBJECT).join();

		verify(mockClient)
				.deleteItem(DeleteItemRequest.builder()
						.tableName("test")
						.key(KEY_MAP)
						.build());
	}

	@Test
	@SneakyThrows
	void shouldDeleteItemFromKeyObjectWithConsumer() {
		when(mockClient.deleteItem(any(DeleteItemRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						DeleteItemResponse.builder().build()));

		mapper.deleteItemFromKeyObject(KEY_OBJECT, b -> b.returnValues("ALL_OLD"))
				.join();

		verify(mockClient).deleteItem(any(DeleteItemRequest.class));
	}

	// scan

	@Test
	@SneakyThrows
	void shouldScanFromRequest() {
		when(mockClient.scan(any(ScanRequest.class)))
				.thenReturn(
						CompletableFuture.completedFuture(ScanResponse.builder().build()));
		var request = ScanRequest.builder().build();

		mapper.scan(request, TestObject.class).join();

		verify(mockClient).scan(ScanRequest.builder().tableName("test").build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldScanFromConsumer() {
		when(mockClient.scan(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<ScanRequest.Builder> consumer = inv.getArgument(0);
			var builder = ScanRequest.builder();
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(ScanResponse.builder().build());
		});

		mapper.scan(b -> {}, TestObject.class).join();

		verify(mockClient).scan(any(Consumer.class));
	}

	// query

	@Test
	@SneakyThrows
	void shouldQueryFromRequest() {
		when(mockClient.query(any(QueryRequest.class)))
				.thenReturn(CompletableFuture.completedFuture(
						QueryResponse.builder().build()));
		var request = QueryRequest.builder().build();

		mapper.query(request, TestObject.class).join();

		verify(mockClient).query(QueryRequest.builder().tableName("test").build());
	}

	@Test
	@SuppressWarnings("unchecked")
	@SneakyThrows
	void shouldQueryFromConsumer() {
		when(mockClient.query(any(Consumer.class))).thenAnswer(inv -> {
			Consumer<QueryRequest.Builder> consumer = inv.getArgument(0);
			var builder = QueryRequest.builder();
			consumer.accept(builder);
			assertEquals("test", builder.build().tableName());
			return CompletableFuture.completedFuture(QueryResponse.builder().build());
		});

		mapper.query(b -> {}, TestObject.class).join();

		verify(mockClient).query(any(Consumer.class));
	}

	@Test
	void builderShouldCreateMapper() {
		assertTrue(DynamoAsyncMapper.builder() instanceof DynamoAsyncMapperBuilder);
	}
}
