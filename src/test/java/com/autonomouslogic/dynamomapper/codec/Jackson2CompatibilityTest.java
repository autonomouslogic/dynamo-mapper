package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

/**
 * Jackson 3 uses the same annotations as Jackson 2, making this test redundant.
 * It exists to ensure that backwards compatibility continues to exist with future releases on Jackson 3.
 */
public class Jackson2CompatibilityTest {
	JsonMapper jsonMapper = StdObjectMapper.jsonMapper();
	ReflectionUtil reflectionUtil = new ReflectionUtil(jsonMapper, null);
	DynamoEncoder encoder = new DynamoEncoder(jsonMapper, reflectionUtil);
	DynamoDecoder decoder = new DynamoDecoder(jsonMapper);

	@Data
	static class Jackson2Object {
		@JsonProperty("my_string")
		private String myString;

		@JsonProperty("my_number")
		private int myNumber;
	}

	@Test
	@SneakyThrows
	void shouldEncodeAndDecodeJackson2AnnotatedObject() {
		var obj = new Jackson2Object();
		obj.myString("hello");
		obj.myNumber(42);

		var encoded = encoder.encode(obj);

		assertEquals("hello", encoded.get("my_string").s());
		assertEquals("42", encoded.get("my_number").n());

		var decoded = decoder.decode(encoded, Jackson2Object.class);

		assertEquals(obj, decoded);
	}
}
