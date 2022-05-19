package com.autonomouslogic.dynamomapper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoTableName("integration-test-table")
public class IntegrationTestObject {
	@DynamoHashKey
	@JsonProperty
	String partitionKey;

	@JsonProperty
	Integer i;

	@JsonProperty
	String str;

	@JsonProperty
	Double dbl;

	@JsonProperty
	BigInteger bigint;

	@JsonProperty
	BigDecimal bigdec;

	@JsonProperty
	byte[] bytes;

	@JsonProperty
	List<String> listString;

	@JsonProperty
	long ttl;
}
