package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestHelper;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

public class DynamoMapperIntegrationTestOld {
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

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void shouldPutAndGetAndUpdateAndDelete(IntegrationTestObject obj) {
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
