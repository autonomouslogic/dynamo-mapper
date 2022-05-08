package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.DynamoMapper;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.test.EncodeDecode;
import com.autonomouslogic.dynamomapper.test.TestObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoEncoderTest {
	DynamoMapper mapper;

	@BeforeEach
	public void beforeEach() {
		var objectMapper = new ObjectMapper();
		mapper = new DynamoMapper(objectMapper);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	public void shouldDecode(CodecTests test) {
		var pojo = mapper.decode(test.getDdb(), TestObject.class);
		assertEquals(test.getPojo(), pojo);
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values())
			.filter(CodecTests::encodeTest)
			.collect(Collectors.toList());
	}
}
