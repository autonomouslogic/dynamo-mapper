package com.autonomouslogic.dynamomapper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoTableName("integration-test-table")
@DynamoDBTable(tableName = "integration-test-table") // v1
@DynamoDbBean // v2
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CompatibilityTestObject {
	@DynamoHashKey
	@JsonProperty
	@DynamoDBHashKey // v1
	String partitionKey;

	@DynamoDBAttribute // v1
	@JsonProperty
	long ttl;

	@DynamoDbPartitionKey // v2
	public String getPartitionKey() {
		return partitionKey;
	}

	public CompatibilityTestObject setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
		return this;
	}

	public long getTtl() {
		return ttl;
	}

	public CompatibilityTestObject setTtl(long ttl) {
		this.ttl = ttl;
		return this;
	}
}
