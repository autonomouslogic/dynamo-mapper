package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoTableName("integration-test-table")
public class IntegrationTestObject {
	@DynamoPrimaryKey
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
	Number num;

	@JsonProperty
	long ttl;
}
