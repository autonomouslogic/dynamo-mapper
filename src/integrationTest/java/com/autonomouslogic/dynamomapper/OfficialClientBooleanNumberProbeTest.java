package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.autonomouslogic.dynamomapper.model.CompatibilityTestObject;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * Shows how the official v1 and v2 clients read a boolean stored as N.
 */
public class OfficialClientBooleanNumberProbeTest {
	static DynamoDbClient client;
	static DynamoDBMapper v1Client;
	static DynamoDbTable<CompatibilityTestObject> v2Table;

	@BeforeAll
	public static void setup() {
		client = IntegrationTestUtil.client();
		v1Client = new DynamoDBMapper(IntegrationTestUtil.clientV1());
		v2Table = DynamoDbEnhancedClient.builder()
				.dynamoDbClient(client)
				.build()
				.table("integration-test-table", TableSchema.fromBean(CompatibilityTestObject.class));
	}

	@Test
	void v1ReadsOneAsTrue() {
		var loaded = v1Client.load(CompatibilityTestObject.class, putBooleansAsNumber("1"));
		assertEquals(true, loaded.isBoolPrimitive());
		assertEquals(true, loaded.getBoolBoxed());
	}

	@Test
	void v1ReadsZeroAsFalse() {
		var loaded = v1Client.load(CompatibilityTestObject.class, putBooleansAsNumber("0"));
		assertEquals(false, loaded.isBoolPrimitive());
		assertEquals(false, loaded.getBoolBoxed());
	}

	@Test
	void v1ReadsTwoAsFalse() {
		var loaded = v1Client.load(CompatibilityTestObject.class, putBooleansAsNumber("2"));
		assertEquals(false, loaded.isBoolPrimitive());
		assertEquals(false, loaded.getBoolBoxed());
	}

	@Test
	void v2ReadsOneAsTrue() {
		var loaded = v2Table.getItem(key(putBooleansAsNumber("1")));
		assertEquals(true, loaded.isBoolPrimitive());
		assertEquals(true, loaded.getBoolBoxed());
	}

	@Test
	void v2ReadsZeroAsFalse() {
		var loaded = v2Table.getItem(key(putBooleansAsNumber("0")));
		assertEquals(false, loaded.isBoolPrimitive());
		assertEquals(false, loaded.getBoolBoxed());
	}

	@Test
	void v2ThrowsOnTwo() {
		var key = key(putBooleansAsNumber("2"));
		var e = assertThrows(IllegalArgumentException.class, () -> v2Table.getItem(key));
		assertEquals("Number could not be converted to boolean: 2", e.getMessage());
	}

	private static String putBooleansAsNumber(String n) {
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
		return key;
	}

	private static Key key(String partitionKey) {
		return Key.builder().partitionValue(partitionKey).build();
	}
}
