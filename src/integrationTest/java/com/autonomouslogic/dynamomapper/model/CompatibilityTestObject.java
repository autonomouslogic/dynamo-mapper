package com.autonomouslogic.dynamomapper.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
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
	Integer i;

	@DynamoDBAttribute // v1
	@JsonProperty
	String str;

	@DynamoDBAttribute // v1
	@JsonProperty
	Double dbl;

	@DynamoDBAttribute // v1
	@JsonProperty
	BigInteger bigint;

	@DynamoDBAttribute // v1
	@JsonProperty
	BigDecimal bigdec;

	@DynamoDBAttribute // v1
	@JsonProperty
	byte[] bytes;

	@JsonProperty
	List<String> listString;

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

	public Integer getI() {
		return i;
	}

	public CompatibilityTestObject setI(Integer i) {
		this.i = i;
		return this;
	}

	public String getStr() {
		return str;
	}

	public CompatibilityTestObject setStr(String str) {
		this.str = str;
		return this;
	}

	public Double getDbl() {
		return dbl;
	}

	public CompatibilityTestObject setDbl(Double dbl) {
		this.dbl = dbl;
		return this;
	}

	public BigInteger getBigint() {
		return bigint;
	}

	public CompatibilityTestObject setBigint(BigInteger bigint) {
		this.bigint = bigint;
		return this;
	}

	public BigDecimal getBigdec() {
		return bigdec;
	}

	public CompatibilityTestObject setBigdec(BigDecimal bigdec) {
		this.bigdec = bigdec;
		return this;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public CompatibilityTestObject setBytes(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}

	public List<String> getListString() {
		return listString;
	}

	public CompatibilityTestObject setListString(List<String> listString) {
		this.listString = listString;
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
