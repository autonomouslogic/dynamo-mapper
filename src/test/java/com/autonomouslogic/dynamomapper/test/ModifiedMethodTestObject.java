package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifiedMethodTestObject {
	private String hashKey;

	@DynamoHashKey
	@JsonProperty("hash_key")
	public String getHashKey() {
		return hashKey;
	}

	public ModifiedMethodTestObject setHashKey(String hashKey) {
		this.hashKey = hashKey;
		return this;
	}
}
