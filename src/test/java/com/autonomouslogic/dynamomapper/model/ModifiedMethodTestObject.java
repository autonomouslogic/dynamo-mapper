package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoTableName("test")
public class ModifiedMethodTestObject {
	private String primaryKey;

	@DynamoPrimaryKey
	@JsonProperty("primary_key")
	public String getPrimaryKey() {
		return primaryKey;
	}

	public ModifiedMethodTestObject setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}
}
