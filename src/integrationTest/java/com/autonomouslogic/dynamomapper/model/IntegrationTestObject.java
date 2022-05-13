package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@DynamoTableName("integration-test-table")
public class IntegrationTestObject {
	@DynamoHashKey
	@JsonProperty
	String partitionKey;

	@JsonProperty
	long ttl;
}
