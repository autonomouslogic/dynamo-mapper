package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
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

public class DynamoAsyncMapperIntegrationTest {
	static DynamoAsyncMapper dynamoAsyncMapper;
	static RandomGenerator rng;

	@BeforeAll
	public static void setup() {
		dynamoAsyncMapper = new DynamoAsyncMapper(IntegrationTestUtil.asyncClient(), StdObjectMapper.objectMapper());
		rng = new ISAACRandom();
	}

	@Test
	@SneakyThrows
	public void shouldPutAndGet() {
		var obj = IntegrationTestObject.builder()
			.partitionKey("key-" + Math.abs(rng.nextLong()))
			.ttl(Instant.now().plus(Duration.ofMinutes(1)).getEpochSecond())
			.build();
		dynamoAsyncMapper.putItem(obj).join();
		var loaded = dynamoAsyncMapper.getItem(obj.partitionKey(), IntegrationTestObject.class).join();
		assertEquals(obj, loaded.item());
	}
}
