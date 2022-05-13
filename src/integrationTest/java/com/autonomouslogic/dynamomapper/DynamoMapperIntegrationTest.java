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
	public void shouldPutAndGet() {
		var obj = IntegrationTestObjects.simple();
		dynamoMapper.putItem(obj);
		var loaded = dynamoMapper.getItem(obj.partitionKey(), IntegrationTestObject.class);
		assertEquals(obj, loaded.item());
	}
}