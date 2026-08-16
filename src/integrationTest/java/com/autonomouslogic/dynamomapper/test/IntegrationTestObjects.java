package com.autonomouslogic.dynamomapper.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.model.MappedBatchGetItemResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationTestObjects {
	public static IntegrationTestObject setKeyAndTtl(IntegrationTestObject obj) {
		return obj.toBuilder()
				.partitionKey(IntegrationTestUtil.partitionKey("integration"))
				.ttl(IntegrationTestUtil.ttl())
				.build();
	}

	public static List<IntegrationTestObject> toKeyObjects(List<String> keys) {
		return keys.stream()
				.map(k -> IntegrationTestObject.builder().partitionKey(k).build())
				.collect(Collectors.toList());
	}

	public static void assertBatchKeys(MappedBatchGetItemResponse<IntegrationTestObject> result, List<String> keys) {
		var fetchedKeys = result.items().values().stream()
				.flatMap(Collection::stream)
				.map(IntegrationTestObject::partitionKey)
				.collect(Collectors.toList());
		assertEquals(new HashSet<>(keys), new HashSet<>(fetchedKeys));
	}
}
