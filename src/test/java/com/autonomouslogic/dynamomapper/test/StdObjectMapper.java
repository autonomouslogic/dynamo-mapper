package com.autonomouslogic.dynamomapper.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StdObjectMapper {
	public static ObjectMapper objectMapper() {
		var mapper = new ObjectMapper();
		mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
		return mapper;
	}
}
