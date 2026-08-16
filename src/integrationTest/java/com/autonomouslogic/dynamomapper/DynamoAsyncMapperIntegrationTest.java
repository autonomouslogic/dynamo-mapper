package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.model.MappedBatchGetItemResponse;
import com.autonomouslogic.dynamomapper.test.IntegrationTestHelper;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;

public class DynamoAsyncMapperIntegrationTest {
	static DynamoAsyncMapper dynamoAsyncMapper;
	static IntegrationTestHelper helper;
	static DynamoEncoder encoder;

	@BeforeAll
	public static void setup() {
		dynamoAsyncMapper = DynamoAsyncMapper.builder()
				.client(IntegrationTestUtil.asyncClient())
				.build();
		encoder = dynamoAsyncMapper.encoder;
		helper = new IntegrationTestHelper();
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.PermutationTester#objectAndTestMethods")
	@SneakyThrows
	void shouldPutAndGetAndUpdateAndDelete(PermutationTester.ObjectAndTestMethod test) {
		var obj = IntegrationTestObjects.setKeyAndTtl(test.obj());
		System.out.println(test);
		new PermutationTester(encoder)
				.obj(obj)
				.methodType(test.methodType())
				.callMethod(test.callMethod())
				.getItemRequestStraight(
						(req, clazz) -> dynamoAsyncMapper.getItem(req, clazz).join())
				.getItemRequestConsumer((consumer, clazz) ->
						dynamoAsyncMapper.getItem(consumer, clazz).join())
				.getItemPrimaryKeyStraight((key, clazz) ->
						dynamoAsyncMapper.getItemFromPrimaryKey(key, clazz).join())
				.getItemPrimaryKeyConsumer((key, consumer, clazz) -> dynamoAsyncMapper
						.getItemFromPrimaryKey(key, consumer, clazz)
						.join())
				.getItemKeyObjectStraight(
						(key) -> dynamoAsyncMapper.getItemFromKeyObject(key).join())
				.getItemKeyObjectConsumer((key, consumer) ->
						dynamoAsyncMapper.getItemFromKeyObject(key, consumer).join())
				.putItemRequestStraight(
						(req, clazz) -> dynamoAsyncMapper.putItem(req, clazz).join())
				.putItemRequestConsumer((consumer, clazz) ->
						dynamoAsyncMapper.putItem(consumer, clazz).join())
				.putItemKeyObjectStraight(
						(key) -> dynamoAsyncMapper.putItemFromKeyObject(key).join())
				.putItemKeyObjectConsumer((key, consumer) ->
						dynamoAsyncMapper.putItemFromKeyObject(key, consumer).join())
				.updateItemRequestStraight(
						(req, clazz) -> dynamoAsyncMapper.updateItem(req, clazz).join())
				.updateItemRequestConsumer((consumer, clazz) ->
						dynamoAsyncMapper.updateItem(consumer, clazz).join())
				.updateItemKeyObjectStraight(
						(key) -> dynamoAsyncMapper.updateItemFromKeyObject(key).join())
				.updateItemKeyObjectConsumer((key, consumer) ->
						dynamoAsyncMapper.updateItemFromKeyObject(key, consumer).join())
				.deleteItemRequestStraight(
						(req, clazz) -> dynamoAsyncMapper.deleteItem(req, clazz).join())
				.deleteItemRequestConsumer((consumer, clazz) ->
						dynamoAsyncMapper.deleteItem(consumer, clazz).join())
				.deleteItemPrimaryKeyStraight((key, clazz) ->
						dynamoAsyncMapper.deleteItemFromPrimaryKey(key, clazz).join())
				.deleteItemPrimaryKeyConsumer((key, consumer, clazz) -> dynamoAsyncMapper
						.deleteItemFromPrimaryKey(key, consumer, clazz)
						.join())
				.deleteItemKeyObjectStraight(
						(key) -> dynamoAsyncMapper.deleteItemFromKeyObject(key).join())
				.deleteItemKeyObjectConsumer((key, consumer) ->
						dynamoAsyncMapper.deleteItemFromKeyObject(key, consumer).join())
				.runTest();
	}

	@Test
	@SneakyThrows
	void shouldScan() {
		String shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(
					IntegrationTestObject.builder().str(shared).build());
			dynamoAsyncMapper.putItemFromKeyObject(obj).join();
		}
		var scanResult = dynamoAsyncMapper
				.scan(
						req -> {
							assertEquals("integration-test-table", req.build().tableName());
						},
						IntegrationTestObject.class)
				.join();
		var filtered = scanResult.items().stream()
				.filter(o -> o.str() != null)
				.filter(o -> o.str().equals(shared))
				.collect(Collectors.toList());
		assertEquals(n, filtered.size());
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItems() {
		String shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		var keys = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(
					IntegrationTestObject.builder().str(shared).build());
			dynamoAsyncMapper.putItemFromKeyObject(obj).join();
			keys.add(obj.partitionKey());
		}
		var batchGetResult = dynamoAsyncMapper
				.batchGetItemFromPrimaryKeys(
						keys,
						req -> {
							var table = req.build().requestItems().keySet();
							assertEquals(Set.of("integration-test-table"), table);
						},
						IntegrationTestObject.class)
				.join();
		var fetchedKeys = batchGetResult.items().values().stream()
				.flatMap(Collection::stream)
				.map(item -> item.partitionKey())
				.collect(Collectors.toList());
		assertEquals(new HashSet<>(keys), new HashSet<>(fetchedKeys));
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemViaRequest() {
		var keys = putBatchItems(3);
		var keyMaps = encodeKeys(keys);
		var req = BatchGetItemRequest.builder()
				.requestItems(Map.of(
						"integration-test-table",
						KeysAndAttributes.builder().keys(keyMaps).build()))
				.build();
		var result =
				dynamoAsyncMapper.batchGetItem(req, IntegrationTestObject.class).join();
		assertBatchKeys(result, keys);
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemViaConsumer() {
		var keys = putBatchItems(3);
		var keyMaps = encodeKeys(keys);
		var result = dynamoAsyncMapper
				.batchGetItem(
						req -> req.requestItems(Map.of(
								"integration-test-table",
								KeysAndAttributes.builder().keys(keyMaps).build())),
						IntegrationTestObject.class)
				.join();
		assertBatchKeys(result, keys);
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemsFromPrimaryKeysWithoutConsumer() {
		var keys = putBatchItems(3);
		var result = dynamoAsyncMapper
				.batchGetItemFromPrimaryKeys(keys, IntegrationTestObject.class)
				.join();
		assertBatchKeys(result, keys);
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemsFromKeyObjects() {
		var keys = putBatchItems(3);
		var keyObjects = toKeyObjects(keys);
		MappedBatchGetItemResponse<IntegrationTestObject> result = dynamoAsyncMapper
				.<IntegrationTestObject>batchGetItemFromKeyObjects(keyObjects)
				.join();
		assertBatchKeys(result, keys);
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItemsFromKeyObjectsWithConsumer() {
		var keys = putBatchItems(3);
		var keyObjects = toKeyObjects(keys);
		MappedBatchGetItemResponse<IntegrationTestObject> result = dynamoAsyncMapper
				.<IntegrationTestObject>batchGetItemFromKeyObjects(
						keyObjects,
						req -> assertEquals(
								Set.of("integration-test-table"),
								req.build().requestItems().keySet()))
				.join();
		assertBatchKeys(result, keys);
	}

	@SneakyThrows
	private List<String> putBatchItems(int n) {
		var keys = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(IntegrationTestObject.builder().build());
			dynamoAsyncMapper.putItemFromKeyObject(obj).join();
			keys.add(obj.partitionKey());
		}
		return keys;
	}

	@SneakyThrows
	private List<Map<String, software.amazon.awssdk.services.dynamodb.model.AttributeValue>> encodeKeys(
			List<String> keys) {
		var keyMaps =
				new ArrayList<Map<String, software.amazon.awssdk.services.dynamodb.model.AttributeValue>>(keys.size());
		for (var k : keys) {
			keyMaps.add(encoder.encodeKeyValue(k, IntegrationTestObject.class));
		}
		return keyMaps;
	}

	private List<IntegrationTestObject> toKeyObjects(List<String> keys) {
		return keys.stream()
				.map(k -> IntegrationTestObject.builder().partitionKey(k).build())
				.collect(Collectors.toList());
	}

	private void assertBatchKeys(MappedBatchGetItemResponse<IntegrationTestObject> result, List<String> keys) {
		var fetchedKeys = result.items().values().stream()
				.flatMap(Collection::stream)
				.map(IntegrationTestObject::partitionKey)
				.collect(Collectors.toList());
		assertEquals(new HashSet<>(keys), new HashSet<>(fetchedKeys));
	}

	@Test
	@SneakyThrows
	void shouldQuery() {
		var obj = IntegrationTestObjects.setKeyAndTtl(
				IntegrationTestObject.builder().str("str-1234").build());
		dynamoAsyncMapper.putItemFromKeyObject(obj).join();
		var queryResult = dynamoAsyncMapper
				.query(
						req -> {
							helper.prepQueryTest(obj, req);
						},
						IntegrationTestObject.class)
				.join();
		assertEquals(List.of(obj), queryResult.items());
	}
}
