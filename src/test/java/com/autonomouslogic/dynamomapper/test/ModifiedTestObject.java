package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModifiedTestObject {
	@DynamoHashKey
	@JsonProperty("hash_key")
	private String hashKey;
}
