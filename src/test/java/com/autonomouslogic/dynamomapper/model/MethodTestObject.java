package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoTableName("test")
public class MethodTestObject {
	private String hashKey;

	@DynamoHashKey
	@JsonProperty
	public String getHashKey() {
		return hashKey;
	}

	public MethodTestObject setHashKey(String hashKey) {
		this.hashKey = hashKey;
		return this;
	}
}
