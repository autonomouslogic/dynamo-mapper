package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoTableName("test")
public class MethodTestObject {
	private String primaryKey;

	@DynamoPrimaryKey
	@JsonProperty
	public String getPrimaryKey() {
		return primaryKey;
	}

	public MethodTestObject setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
		return this;
	}
}
