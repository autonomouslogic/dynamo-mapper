package com.autonomouslogic.dynamomapper;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.autonomouslogic.dynamomapper.model.CompatibilityTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompatibilityTest {
	static DynamoMapper dynamoMapper;
	static DynamoDBMapper v1Client;
	static DynamoDbEnhancedClient v2Client;
	static TableSchema<CompatibilityTestObject> v2Schema;
	static DynamoDbTable<CompatibilityTestObject> v2Table;

	@BeforeAll
	public static void setup() {
		dynamoMapper = DynamoMapper.builder()
			.client(IntegrationTestUtil.client())
			.build();
		v1Client = new DynamoDBMapper(IntegrationTestUtil.clientV1());
		v2Client = DynamoDbEnhancedClient.builder()
			.dynamoDbClient(IntegrationTestUtil.client())
			.build();
		v2Schema = TableSchema.fromClass(CompatibilityTestObject.class);
		assertEquals("partitionKey", v2Schema.tableMetadata().primaryPartitionKey());
		v2Table = v2Client.table("integration-test-table", v2Schema);
	}

	@Test
	@SneakyThrows
	public void shouldReadFromV1() {
		var obj = IntegrationTestObjects.compatibility();
		v1Client.save(obj);
		assertEquals(obj, dynamoMapper.getItem(obj.getPartitionKey(), CompatibilityTestObject.class).item());
	}

	@Test
	@SneakyThrows
	public void shouldWriteToV1() {
		var obj = IntegrationTestObjects.compatibility();
		dynamoMapper.putItem(obj);
		assertEquals(obj, v1Client.load(CompatibilityTestObject.class, obj.getPartitionKey()));
	}

	@Test
	@SneakyThrows
	public void shouldReadFromV2() {
		var obj = IntegrationTestObjects.compatibility();
		v2Table.putItem(obj);
		assertEquals(obj, dynamoMapper.getItem(obj.getPartitionKey(), CompatibilityTestObject.class).item());
	}

	@Test
	@SneakyThrows
	public void shouldWriteToV2() {
		var obj = IntegrationTestObjects.compatibility();
		dynamoMapper.putItem(obj);
		assertEquals(obj, v2Table.getItem(obj));
	}
}
