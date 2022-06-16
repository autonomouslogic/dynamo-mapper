package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.DynamoMapper;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTestHelper {
	private final DynamoDbClient client = IntegrationTestUtil.client();
	private final DynamoMapper dynamoMapper = DynamoMapper.builder().client(client).build();

	public void prepQueryTest(IntegrationTestObject obj, QueryRequest.Builder req) {
		assertEquals("integration-test-table", req.build().tableName());
		req
			.keyConditionExpression("partitionKey = :v")
			.filterExpression("str = :s")
			.expressionAttributeValues(Map.of(
				":v", AttributeValue.builder().s(obj.partitionKey()).build(),
				":s", AttributeValue.builder().s(obj.str()).build()
			));
	}
}
