package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import com.autonomouslogic.dynamomapper.test.StdObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoAsyncMapperIntegrationTest {
	static DynamoAsyncMapper dynamoAsyncMapper;

	@BeforeAll
	public static void setup() {
		dynamoAsyncMapper = new DynamoAsyncMapper(IntegrationTestUtil.asyncClient(), StdObjectMapper.objectMapper());
	}

	@Test
	@SneakyThrows
	public void shouldPutAndGetAndDelete() {
		var obj = IntegrationTestObjects.simple();
		// Put.
		dynamoAsyncMapper.putItem(obj).join();
		// Get.
		var getResponse = dynamoAsyncMapper.getItem(obj.partitionKey(), IntegrationTestObject.class).join();
		assertEquals(obj, getResponse.item());
		// Delete.
		var deleteResponse = dynamoAsyncMapper.deleteItem(obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class).join();
		assertEquals(obj, deleteResponse.item());
	}
}
