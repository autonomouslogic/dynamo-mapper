package com.autonomouslogic.dynamomapper.model;


import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@DynamoTableName("test")
@Data
@Accessors(chain = true)
public class ModifiedTestObject {
	@DynamoHashKey
	@JsonProperty("hash_key")
	private String hashKey;
}
