package com.autonomouslogic.dynamomapper.test;


import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;

public class IntegrationTestObjects {
	public static IntegrationTestObject setKeyAndTtl(IntegrationTestObject obj) {
		return obj.toBuilder()
				.partitionKey(IntegrationTestUtil.partitionKey("integration"))
				.ttl(IntegrationTestUtil.ttl())
				.build();
	}
}
