package com.autonomouslogic.dynamomapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestObject {
	@JsonProperty
	protected String string;
}
