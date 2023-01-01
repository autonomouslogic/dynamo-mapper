package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
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
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

public class DynamoMapperIntegrationTest {
	private enum MethodType {
		REQUEST,
		PRIMARY_KEY,
		KEY_OBJECT
	}

	private enum CallMethod {
		STRAIGHT,
		CONSUMER
	}

	@Value
	private static final class ObjectAndTestMethod {
		IntegrationTestObject obj;
		MethodType methodType;
		CallMethod callMethod;
	}

	static DynamoMapper dynamoMapper;
	static DynamoEncoder encoder;
	static IntegrationTestHelper helper;

	@BeforeAll
	public static void setup() {
		dynamoMapper =
				DynamoMapper.builder().client(IntegrationTestUtil.client()).build();
		encoder = dynamoMapper.encoder;
		helper = new IntegrationTestHelper();
	}

	public static List<ObjectAndTestMethod> objectAndTestMethods() {
		return IntegrationTestUtil.loadIntegrationTestObjects().stream()
				.flatMap(obj -> Stream.of(MethodType.values()).flatMap(methodType -> Stream.of(CallMethod.values())
						.map(callMethod -> new ObjectAndTestMethod(obj, methodType, callMethod))))
				.collect(Collectors.toList());
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void shouldPutAndGetAndUpdateAndDelete_old(IntegrationTestObject obj) {
		obj = IntegrationTestObjects.setKeyAndTtl(obj);
		System.out.println(obj);
		// Put.
		dynamoMapper.putItemFromKeyObject(obj);
		// Get.
		var getResponse = dynamoMapper.getItemFromPrimaryKey(obj.partitionKey(), IntegrationTestObject.class);
		assertEquals(obj, getResponse.item());
		// Update.
		var obj2 = obj.toBuilder().str("new-val").build();
		var updateResponse = dynamoMapper.updateItemFromKeyObject(obj2, req -> req.returnValues(ReturnValue.ALL_OLD));
		assertEquals(obj, updateResponse.item());
		// Delete.
		var deleteResponse = dynamoMapper.deleteItemFromPrimaryKey(
				obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class);
		assertEquals(obj2, deleteResponse.item());
	}

	@ParameterizedTest
	@MethodSource("objectAndTestMethods")
	@SneakyThrows
	void shouldPutAndGetAndUpdateAndDelete(ObjectAndTestMethod test) {
		var obj = IntegrationTestObjects.setKeyAndTtl(test.obj());
		System.out.println(obj);
		// Put.
		testPut(obj, test.methodType(), test.callMethod());
		// Get.
		var getResponse = testGet(obj, test.methodType(), test.callMethod());
		assertEquals(obj, getResponse.item());
		// Update.
		var obj2 = obj.toBuilder().str("new-val").build();
		var updateResponse = dynamoMapper.updateItemFromKeyObject(obj2, req -> req.returnValues(ReturnValue.ALL_OLD));
		assertEquals(obj, updateResponse.item());
		// Delete.
		var deleteResponse = dynamoMapper.deleteItemFromPrimaryKey(
				obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class);
		assertEquals(obj2, deleteResponse.item());
	}

	@SneakyThrows
	private MappedGetItemResponse<IntegrationTestObject> testGet(
			IntegrationTestObject obj, MethodType methodType, CallMethod callMethod) {
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return dynamoMapper.getItem(
								GetItemRequest.builder()
										.key(Map.of(
												"partitionKey",
												AttributeValue.builder()
														.s(obj.partitionKey())
														.build()))
										.build(),
								IntegrationTestObject.class);
					case CONSUMER:
						return dynamoMapper.getItem(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.key(Map.of(
											"partitionKey",
											AttributeValue.builder()
													.s(obj.partitionKey())
													.build()));
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY:
				switch (callMethod) {
					case STRAIGHT:
						return dynamoMapper.getItemFromPrimaryKey(obj.partitionKey(), IntegrationTestObject.class);
					case CONSUMER:
						return dynamoMapper.getItemFromPrimaryKey(
								obj.partitionKey(),
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									assertEquals(
											obj.partitionKey(),
											req.key().get("partitionKey").s());
								},
								IntegrationTestObject.class);
				}
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return dynamoMapper.getItemFromKeyObject(obj);
					case CONSUMER:
						return dynamoMapper.getItemFromKeyObject(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(
									obj.partitionKey(),
									req.key().get("partitionKey").s());
						});
				}
		}
		throw new IllegalStateException();
	}

	@SneakyThrows
	private MappedPutItemResponse<IntegrationTestObject> testPut(
			IntegrationTestObject obj, MethodType methodType, CallMethod callMethod) {
		var item = encoder.encode(obj);
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return dynamoMapper.putItem(
								PutItemRequest.builder().item(item).build(), IntegrationTestObject.class);
					case CONSUMER:
						return dynamoMapper.putItem(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.item(item);
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY: // Puts don't have a straight primary-key method, just use key objects.
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return dynamoMapper.putItemFromKeyObject(obj);
					case CONSUMER:
						return dynamoMapper.putItemFromKeyObject(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(item, req.item());
						});
				}
		}
		throw new IllegalStateException();
	}

	@Test
	@SneakyThrows
	void shouldScan() {
		var shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(
					IntegrationTestObject.builder().str(shared).build());
			dynamoMapper.putItemFromKeyObject(obj);
		}
		var scanResult = dynamoMapper.scan(
				req -> {
					assertEquals("integration-test-table", req.build().tableName());
				},
				IntegrationTestObject.class);
		var filtered = scanResult.items().stream()
				.filter(o -> o.str() != null)
				.filter(o -> o.str().equals(shared))
				.collect(Collectors.toList());
		assertEquals(n, filtered.size());
	}

	@Test
	@SneakyThrows
	void shouldBatchGetItems() {
		var shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		var keys = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(
					IntegrationTestObject.builder().str(shared).build());
			dynamoMapper.putItemFromKeyObject(obj);
			keys.add(obj.partitionKey());
		}
		var batchGetResult = dynamoMapper.batchGetItemFromPrimaryKeys(
				keys,
				req -> {
					var table = req.build().requestItems().keySet();
					assertEquals(Set.of("integration-test-table"), table);
				},
				IntegrationTestObject.class);
		var fetchedKeys = batchGetResult.items().values().stream()
				.flatMap(Collection::stream)
				.map(item -> item.partitionKey())
				.collect(Collectors.toList());
		assertEquals(new HashSet<>(keys), new HashSet<>(fetchedKeys));
	}

	@Test
	@SneakyThrows
	void shouldQuery() {
		var obj = IntegrationTestObjects.setKeyAndTtl(
				IntegrationTestObject.builder().str("str-1234").build());
		dynamoMapper.putItemFromKeyObject(obj);
		var queryResult = dynamoMapper.query(
				req -> {
					helper.prepQueryTest(obj, req);
				},
				IntegrationTestObject.class);
		assertEquals(List.of(obj), queryResult.items());
	}
}
