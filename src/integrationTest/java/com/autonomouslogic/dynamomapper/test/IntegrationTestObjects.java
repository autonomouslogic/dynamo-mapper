package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.model.CompatibilityTestObject;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;

import java.time.Duration;
import java.time.Instant;

public class IntegrationTestObjects {
	public static IntegrationTestObject simple() {
		return IntegrationTestObject.builder()
			.partitionKey(IntegrationTestUtil.partitionKey("integration"))
			.ttl(IntegrationTestUtil.ttl())
			.build();
	}

	public static CompatibilityTestObject compatibility() {
		return new CompatibilityTestObject()
			.setPartitionKey(IntegrationTestUtil.partitionKey("compatibility"))
			.setTtl(IntegrationTestUtil.ttl());
	}
}
