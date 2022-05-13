package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.fasterxml.jackson.annotation.JsonProperty;

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
