package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DynamoDecoderTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	DynamoDecoder decoder = new DynamoDecoder(objectMapper);

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldDecode(CodecTests test) {
		var pojo = decoder.decode(test.ddb(), TestObject.class);
		var json = objectMapper.writeValueAsString(pojo);
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values()).filter(CodecTests::decodeTest).collect(Collectors.toList());
	}
}
