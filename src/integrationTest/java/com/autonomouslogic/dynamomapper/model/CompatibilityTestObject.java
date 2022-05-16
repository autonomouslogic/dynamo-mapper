package com.autonomouslogic.dynamomapper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoTableName("integration-test-table")
@DynamoDBTable(tableName = "integration-test-table")
@DynamoDbBean
@EqualsAndHashCode
@ToString
public class CompatibilityTestObject {
	@DynamoHashKey
	@JsonProperty
	@DynamoDBHashKey
	String partitionKey;

	@DynamoDBAttribute
	@JsonProperty
	long ttl;

	@DynamoDbAttribute("partitionKey")
	@DynamoDbPartitionKey
	public String getPartitionKey() {
		return partitionKey;
	}

	public CompatibilityTestObject setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
		return this;
	}

	@DynamoDbAttribute("ttl")
	public long getTtl() {
		return ttl;
	}

	public CompatibilityTestObject setTtl(long ttl) {
		this.ttl = ttl;
		return this;
	}
}
