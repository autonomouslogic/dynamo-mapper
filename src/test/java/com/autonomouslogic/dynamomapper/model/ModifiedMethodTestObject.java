package com.autonomouslogic.dynamomapper.model;


import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoTableName("test")
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
