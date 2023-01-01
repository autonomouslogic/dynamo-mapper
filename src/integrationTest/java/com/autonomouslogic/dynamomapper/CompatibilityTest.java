package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.autonomouslogic.dynamomapper.model.CompatibilityTestObject;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestObjects;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * Tests compatibility between Dynamo Mapper and the v1 and v2 SDKs.
 */
public class CompatibilityTest {
	static DynamoMapper dynamoMapper;
	static DynamoDBMapper v1Client;
	static DynamoDbEnhancedClient v2Client;
	static TableSchema<CompatibilityTestObject> v2Schema;
	static DynamoDbTable<CompatibilityTestObject> v2Table;

	@BeforeAll
	public static void setup() {
		dynamoMapper =
				DynamoMapper.builder().client(IntegrationTestUtil.client()).build();
		v1Client = new DynamoDBMapper(IntegrationTestUtil.clientV1());
		v2Client = DynamoDbEnhancedClient.builder()
				.dynamoDbClient(IntegrationTestUtil.client())
				.build();
		v2Schema = TableSchema.fromBean(CompatibilityTestObject.class);
		v2Table = v2Client.table("integration-test-table", v2Schema);
	}

	@ParameterizedTest
	@MethodSource("loadCompatibilityTestObjects")
	@SneakyThrows
	void shouldReadFromV1(CompatibilityTestObject obj) {
		System.out.println(obj);
		v1Client.save(obj);
		var loaded = dynamoMapper
				.getItemFromPrimaryKey(obj.getPartitionKey(), CompatibilityTestObject.class)
				.item();
		assertEquals(obj, loaded);
	}

	@ParameterizedTest
	@MethodSource("loadCompatibilityTestObjects")
	@SneakyThrows
	void shouldWriteToV1(CompatibilityTestObject obj) {
		System.out.println(obj);
		dynamoMapper.putItemFromKeyObject(obj);
		var loaded = v1Client.load(CompatibilityTestObject.class, obj.getPartitionKey());
		assertEquals(obj, loaded);
	}

	@ParameterizedTest
	@MethodSource("loadCompatibilityTestObjects")
	@SneakyThrows
	void shouldReadFromV2(CompatibilityTestObject obj) {
		System.out.println(obj);
		v2Table.putItem(obj);
		var loaded = dynamoMapper
				.getItemFromPrimaryKey(obj.getPartitionKey(), CompatibilityTestObject.class)
				.item();
		assertEquals(obj, loaded);
	}

	@ParameterizedTest
	@MethodSource("loadCompatibilityTestObjects")
	@SneakyThrows
	void shouldWriteToV2(CompatibilityTestObject obj) {
		System.out.println(obj);
		dynamoMapper.putItemFromKeyObject(obj);
		var loaded = v2Table.getItem(obj);
		assertEquals(obj, loaded);
	}

	@SneakyThrows
	static List<CompatibilityTestObject> loadCompatibilityTestObjects() {
		var objectMapper = StdObjectMapper.objectMapper();
		var list = new ArrayList<CompatibilityTestObject>();
		for (IntegrationTestObject obj : IntegrationTestUtil.loadIntegrationTestObjects()) {
			obj = IntegrationTestObjects.setKeyAndTtl(obj);
			list.add(objectMapper.readValue(objectMapper.writeValueAsString(obj), CompatibilityTestObject.class));
		}
		return list;
	}
}
