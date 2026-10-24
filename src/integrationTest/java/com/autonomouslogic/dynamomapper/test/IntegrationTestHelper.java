package com.autonomouslogic.dynamomapper.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.DynamoMapper;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

public class IntegrationTestHelper {
	private final DynamoDbClient client = IntegrationTestUtil.client();
	private final DynamoMapper dynamoMapper =
			DynamoMapper.builder().client(client).build();

	public static Stream<Arguments> numberAsBooleanCases() {
		return Stream.of(
				Arguments.of("0", false),
				Arguments.of("1", true),
				Arguments.of("2", true),
				Arguments.of("-1", true),
				Arguments.of("100000000", true));
	}

	public void testReadNumberAsBoolean(String n, boolean expected, FailFunction<String, IntegrationTestObject> getItem)
			throws Exception {
		var key = IntegrationTestUtil.partitionKey("integration");
		client.putItem(req -> req.tableName("integration-test-table")
				.item(Map.of(
						"partitionKey", AttributeValue.builder().s(key).build(),
						"ttl",
								AttributeValue.builder()
										.n(Long.toString(IntegrationTestUtil.ttl()))
										.build(),
						"boolPrimitive", AttributeValue.builder().n(n).build(),
						"boolBoxed", AttributeValue.builder().n(n).build())));
		var loaded = getItem.apply(key);
		assertEquals(expected, loaded.boolPrimitive());
		assertEquals(expected, loaded.boolBoxed());
	}

	public void prepQueryTest(IntegrationTestObject obj, QueryRequest.Builder req) {
		assertEquals("integration-test-table", req.build().tableName());
		req.keyConditionExpression("partitionKey = :v")
				.filterExpression("str = :s")
				.expressionAttributeValues(Map.of(
						":v", AttributeValue.builder().s(obj.partitionKey()).build(),
						":s", AttributeValue.builder().s(obj.str()).build()));
	}
}
