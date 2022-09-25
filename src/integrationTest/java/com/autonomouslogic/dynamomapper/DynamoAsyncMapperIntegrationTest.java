package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestHelper;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

public class DynamoAsyncMapperIntegrationTest {
	static DynamoAsyncMapper dynamoAsyncMapper;
	static IntegrationTestHelper helper;

	@BeforeAll
	public static void setup() {
		dynamoAsyncMapper = DynamoAsyncMapper.builder()
				.client(IntegrationTestUtil.asyncClient())
				.build();
		helper = new IntegrationTestHelper();
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void shouldPutAndGetAndDelete(IntegrationTestObject obj) {
		obj = IntegrationTestObjects.setKeyAndTtl(obj);
		System.out.println(obj);
		// Put.
		dynamoAsyncMapper.putItem(obj).join();
		// Get.
		var getResponse = dynamoAsyncMapper
				.getItem(obj.partitionKey(), IntegrationTestObject.class)
				.join();
		assertEquals(obj, getResponse.item());
		// Update.
		var obj2 = obj.toBuilder().str("new-val").build();
		var updateResponse = dynamoAsyncMapper
				.updateItem(obj2, req -> req.returnValues(ReturnValue.ALL_OLD))
				.join();
		assertEquals(obj, updateResponse.item());
		// Delete.
		var deleteResponse = dynamoAsyncMapper
				.deleteItem(
						obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class)
				.join();
		assertEquals(obj2, deleteResponse.item());
	}

	@Test
	@SneakyThrows
	void shouldScan() {
		String shared = Long.toString(IntegrationTestUtil.RNG.nextLong());
		int n = 10;
		for (int i = 0; i < n; i++) {
			var obj = IntegrationTestObjects.setKeyAndTtl(
					IntegrationTestObject.builder().str(shared).build());
			dynamoAsyncMapper.putItem(obj).join();
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
	void shouldQuery() {
		var obj = IntegrationTestObjects.setKeyAndTtl(
				IntegrationTestObject.builder().str("str-1234").build());
		dynamoAsyncMapper.putItem(obj).join();
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
