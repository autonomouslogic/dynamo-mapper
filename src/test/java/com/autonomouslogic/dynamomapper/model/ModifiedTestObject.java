package com.autonomouslogic.dynamomapper.model;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@DynamoTableName("test")
@Data
@Accessors(chain = true)
public class ModifiedTestObject {
	@DynamoPrimaryKey
	@JsonProperty("primary_key")
	private String primaryKey;
}
