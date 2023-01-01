package com.autonomouslogic.dynamomapper.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
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
	@DynamoPrimaryKey
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

	public void setPartitionKey(String partitionKey) {
		this.partitionKey = partitionKey;
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Double getDbl() {
		return dbl;
	}

	public void setDbl(Double dbl) {
		this.dbl = dbl;
	}

	public BigInteger getBigint() {
		return bigint;
	}

	public void setBigint(BigInteger bigint) {
		this.bigint = bigint;
	}

	public BigDecimal getBigdec() {
		return bigdec;
	}

	public void setBigdec(BigDecimal bigdec) {
		this.bigdec = bigdec;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public List<String> getListString() {
		return listString;
	}

	public void setListString(List<String> listString) {
		this.listString = listString;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
}
