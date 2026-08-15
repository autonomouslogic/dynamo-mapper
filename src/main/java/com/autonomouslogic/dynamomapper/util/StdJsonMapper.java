package com.autonomouslogic.dynamomapper.util;

import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.json.JsonMapper;

public class StdJsonMapper {
	public static JsonMapper jsonMapper() {
		return JsonMapper.builder()
				.enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
				.build();
	}
}
