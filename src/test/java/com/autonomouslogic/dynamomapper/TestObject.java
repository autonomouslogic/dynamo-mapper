package com.autonomouslogic.dynamomapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestObject {
	@JsonProperty
	protected byte[] binary;
	@JsonProperty
	protected String string;
}
