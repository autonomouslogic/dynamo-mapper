package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
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
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	protected boolean boolPrimitive;

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
	protected Number number;

	@JsonProperty
	protected Integer integer;

	@JsonProperty
	protected Long longValue;

	@JsonProperty
	protected Float floatValue;

	@JsonProperty
	protected Double doubleValue;

	@JsonProperty
	protected BigInteger bigInteger;

	@JsonProperty
	protected BigDecimal bigDecimal;

	@JsonProperty
	protected List<Number> numberList;

	@JsonProperty
	protected String nul;

	@JsonProperty
	@DynamoPrimaryKey
	protected String string;

	@JsonProperty
	protected List<String> stringList;
}
