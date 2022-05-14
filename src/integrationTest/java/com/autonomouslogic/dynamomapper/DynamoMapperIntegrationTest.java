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

public class DynamoMapperIntegrationTest {
	static DynamoMapper dynamoMapper;

	@BeforeAll
	public static void setup() {
		dynamoMapper = new DynamoMapper(IntegrationTestUtil.client(), StdObjectMapper.objectMapper());
	}

	@Test
	@SneakyThrows
	public void shouldPutAndGetAndDelete() {
		var obj = IntegrationTestObjects.simple();
		// Put.
		var putResponse = dynamoMapper.putItem(obj);
		// Get.
		var getResponse = dynamoMapper.getItem(obj.partitionKey(), IntegrationTestObject.class);
		assertEquals(obj, getResponse.item());
		// Delete.
		var deleteResponse = dynamoMapper.deleteItem(obj.partitionKey(), req -> req.returnValues(ReturnValue.ALL_OLD), IntegrationTestObject.class);
		assertEquals(obj, deleteResponse.item());
	}
}
