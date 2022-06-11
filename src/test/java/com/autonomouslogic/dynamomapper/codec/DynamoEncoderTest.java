package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamoEncoderTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	DynamoEncoder encoder = new DynamoEncoder(StdObjectMapper.objectMapper());

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	public void shouldEncode(CodecTests test) {
		var ddb = encoder.encode(test.pojo());
		assertEquals(test.ddb(), ddb);
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values())
			.filter(CodecTests::encodeTest)
//			.filter(e -> e.name().equals("LIST_OBJECT"))
			.collect(Collectors.toList());
	}

	@Test
	@SneakyThrows
	public void shouldEncodeRealNulls() {
		var attr = encoder.encode(objectMapper.createObjectNode()
			.put("null", (String) null));
		assertTrue(attr.get("null").nul());
	}

	@Test
	@SneakyThrows
	public void shouldEncodeJsonNulls() {
		var json = objectMapper.createObjectNode();
		json.set("null", objectMapper.nullNode());
		var attr = encoder.encode(json);
		assertTrue(attr.get("null").nul());
	}
}
