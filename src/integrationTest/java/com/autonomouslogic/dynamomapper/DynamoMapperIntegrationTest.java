// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

class DynamoMapperIntegrationTest {
	static DynamoDbClient client;

	static DynamoMapper mapper;

	@BeforeAll
	static void beforeAll() {
		client = IntegrationTestUtil.client();
		mapper = DynamoMapper.builder().client(client).build();
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testGetItem_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testGetItem_GetItemRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testBatchGetItem_BatchGetItemRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testBatchGetItem_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testPutItem_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testPutItem_PutItemRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testUpdateItem_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testUpdateItem_UpdateItemRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testDeleteItem_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testDeleteItem_DeleteItemRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testScan_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testScan_ScanRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testQuery_Consumer(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}

	@ParameterizedTest
	@MethodSource("com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
	@SneakyThrows
	void testQuery_QueryRequest(IntegrationTestObject obj) {
		Assertions.fail("todo");
	}
}
