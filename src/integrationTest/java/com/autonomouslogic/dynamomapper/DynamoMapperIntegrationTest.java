package com.autonomouslogic.dynamomapper;

import com.amazonaws.services.dynamodbv2.document.QueryFilter;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoMapperIntegrationTest {
	static DynamoMapper dynamoMapper;
	static DynamoEncoder encoder;

	@BeforeAll
	public static void setup() {
		dynamoMapper = DynamoMapper.builder().client(IntegrationTestUtil.client()).build();
		encoder = dynamoMapper.encoder;
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	public void shouldPutAndGetAndUpdateAndDelete(IntegrationTestObject obj) {
		obj = IntegrationTestObjects.setKeyAndTtl(obj);
		System.out.println(obj);
		// Put.
		dynamoMapper.putItem(obj);
		// Get.
		var getResponse = dynamoMapper.getItem(obj.partitionKey(), IntegrationTestObject.class);
		assertEquals(obj, getResponse.item());
		// Update.
		var obj2 = obj.toBuilder()
			.str("new-val")
			.build();
		var updateResponse = dynamoMapper.updateItem(obj2, req -> req.returnValues(ReturnValue.ALL_OLD));
		assertEquals(obj, updateResponse.item());
		// Delete.
		var deleteResponse = dynamoMapper.deleteItem(obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class);
		assertEquals(obj2, deleteResponse.item());
	}

	@Test
	@SneakyThrows
	public void shouldScan() {
		String shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(IntegrationTestObject.builder()
				.str(shared)
				.build());
			dynamoMapper.putItem(obj);
		}
		var scanResult = dynamoMapper.scan(req -> {
			assertEquals("integration-test-table", req.build().tableName());
		}, IntegrationTestObject.class);
		var filtered = scanResult.items().stream()
			.filter(o -> o.str() != null)
			.filter(o -> o.str().equals(shared))
			.collect(Collectors.toList());
		assertEquals(n, filtered.size());
	}

	@Test
	@SneakyThrows
	public void shouldQuery() {
		var obj = IntegrationTestObjects.setKeyAndTtl(IntegrationTestObject.builder()
			.str("str-1234")
			.build());
		dynamoMapper.putItem(obj);
		var queryResult = dynamoMapper.query(req -> {
			assertEquals("integration-test-table", req.build().tableName());
			req
				.keyConditionExpression("partitionKey = :v")
				.filterExpression("str = :s")
				.expressionAttributeValues(Map.of(
					":v", AttributeValue.builder().s(obj.partitionKey()).build(),
					":s", AttributeValue.builder().s(obj.str()).build()
				));
		}, IntegrationTestObject.class);
		assertEquals(List.of(obj), queryResult.items());
	}
}
