package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;

import java.time.Duration;
import java.time.Instant;

public class IntegrationTestObjects {
	public static IntegrationTestObject simple() {
		return IntegrationTestObject.builder()
			.partitionKey("key-" + Math.abs(IntegrationTestUtil.RNG.nextLong()))
			.ttl(Instant.now().plus(Duration.ofMinutes(1)).getEpochSecond())
			.build();
	}
}
