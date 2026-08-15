package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DynamoEncoderTest {
	JsonMapper jsonMapper = StdObjectMapper.jsonMapper();
	ReflectionUtil reflectionUtil = new ReflectionUtil(jsonMapper, null);
	DynamoEncoder encoder = new DynamoEncoder(StdObjectMapper.jsonMapper(), reflectionUtil);

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldEncode(CodecTests test) {
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
	void shouldEncodeRealNulls() {
		var attr = encoder.encode(jsonMapper.createObjectNode().put("null", (String) null));
		assertTrue(attr.get("null").nul());
	}

	@Test
	@SneakyThrows
	void shouldEncodeJsonNulls() {
		var json = jsonMapper.createObjectNode();
		json.set("null", jsonMapper.nullNode());
		var attr = encoder.encode(json);
		assertTrue(attr.get("null").nul());
	}
}
