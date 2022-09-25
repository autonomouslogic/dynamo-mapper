package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@DynamoTableName("test")
@Data
@Accessors(fluent = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestObject {
	@JsonProperty
	protected byte[] binary;

	@JsonProperty
	protected Boolean bool;

	@JsonProperty
	protected List<byte[]> binaryList;

	@JsonProperty
	protected List<String> listString;

	@JsonProperty
	protected List<TestObject> listObject;

	@JsonProperty
	protected TestObject object;

	@JsonProperty
	protected Map<String, String> map;

	@JsonProperty
	protected Number number; // @todo Long, BigInteger, BigDecimal

	@JsonProperty
	protected List<Number> numberList;

	@JsonProperty
	protected String nul;

	@JsonProperty
	@DynamoHashKey
	protected String string;

	@JsonProperty
	protected List<String> stringList;
}
