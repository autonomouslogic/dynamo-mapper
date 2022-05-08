package com.autonomouslogic.dynamomapper.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestObject {
	@JsonProperty
	protected byte[] binary;
	@JsonProperty
	protected Boolean bool;
	@JsonProperty
	protected List<byte[]> binaryList;
	@JsonProperty
	protected Set<byte[]> binarySet;
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
	protected Set<Number> numberSet;
	@JsonProperty
	protected String nul;
	@JsonProperty
	protected String string;
	@JsonProperty
	protected List<String> stringList;
	@JsonProperty
	protected Set<String> stringSet;
}
