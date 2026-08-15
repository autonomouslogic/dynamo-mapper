package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestHelper;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoMapperIntegrationTest {
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
	@MethodSource("com.autonomouslogic.dynamomapper.PermutationTester#objectAndTestMethods")
	@SneakyThrows
	void shouldPutAndGetAndUpdateAndDelete(PermutationTester.ObjectAndTestMethod test) {
		var obj = IntegrationTestObjects.setKeyAndTtl(test.obj());
		System.out.println(test);
		new PermutationTester(encoder)
				.obj(obj)
				.methodType(test.methodType())
				.callMethod(test.callMethod())
				.getItemRequestStraight((req, clazz) -> dynamoMapper.getItem(req, clazz))
				.getItemRequestConsumer((consumer, clazz) -> dynamoMapper.getItem(consumer, clazz))
				.getItemPrimaryKeyStraight((key, clazz) -> dynamoMapper.getItemFromPrimaryKey(key, clazz))
				.getItemPrimaryKeyConsumer(
						(key, consumer, clazz) -> dynamoMapper.getItemFromPrimaryKey(key, consumer, clazz))
				.getItemKeyObjectStraight((key) -> dynamoMapper.getItemFromKeyObject(key))
				.getItemKeyObjectConsumer((key, consumer) -> dynamoMapper.getItemFromKeyObject(key, consumer))
				.putItemRequestStraight((req, clazz) -> dynamoMapper.putItem(req, clazz))
				.putItemRequestConsumer((consumer, clazz) -> dynamoMapper.putItem(consumer, clazz))
				.putItemKeyObjectStraight((key) -> dynamoMapper.putItemFromKeyObject(key))
				.putItemKeyObjectConsumer((key, consumer) -> dynamoMapper.putItemFromKeyObject(key, consumer))
				.updateItemRequestStraight((req, clazz) -> dynamoMapper.updateItem(req, clazz))
				.updateItemRequestConsumer((consumer, clazz) -> dynamoMapper.updateItem(consumer, clazz))
				.updateItemKeyObjectStraight((key) -> dynamoMapper.updateItemFromKeyObject(key))
				.updateItemKeyObjectConsumer((key, consumer) -> dynamoMapper.updateItemFromKeyObject(key, consumer))
				.deleteItemRequestStraight((req, clazz) -> dynamoMapper.deleteItem(req, clazz))
				.deleteItemRequestConsumer((consumer, clazz) -> dynamoMapper.deleteItem(consumer, clazz))
				.deleteItemPrimaryKeyStraight((key, clazz) -> dynamoMapper.deleteItemFromPrimaryKey(key, clazz))
				.deleteItemPrimaryKeyConsumer(
						(key, consumer, clazz) -> dynamoMapper.deleteItemFromPrimaryKey(key, consumer, clazz))
				.deleteItemKeyObjectStraight((key) -> dynamoMapper.deleteItemFromKeyObject(key))
				.deleteItemKeyObjectConsumer((key, consumer) -> dynamoMapper.deleteItemFromKeyObject(key, consumer))
				.runTest();
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

	// DynamoDB numbers are limited to 38 significant digits.
	// https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html#HowItWorks.DataTypes.Number
	@Test
	@SneakyThrows
	void shouldRejectBigIntegersExceedingDynamoDbPrecision() {
		var obj = IntegrationTestObjects.setKeyAndTtl(IntegrationTestObject.builder()
				.bigint(new BigInteger("999999999999999999999999999999999999999")) // 39 digits
				.build());
		assertThrows(DynamoDbException.class, () -> dynamoMapper.putItemFromKeyObject(obj));
	}

	@Test
	@SneakyThrows
	void shouldRejectBigDecimalsExceedingDynamoDbPrecision() {
		var obj = IntegrationTestObjects.setKeyAndTtl(IntegrationTestObject.builder()
				.bigdec(new BigDecimal("1.23456789012345678901234567890123456789")) // 39 significant digits
				.build());
		assertThrows(DynamoDbException.class, () -> dynamoMapper.putItemFromKeyObject(obj));
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
