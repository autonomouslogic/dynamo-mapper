package com.autonomouslogic.dynamomapper.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.json.JsonMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StdJsonMapper {
	public static JsonMapper jsonMapper() {
		return JsonMapper.builder()
				.enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
				.build();
	}
}
