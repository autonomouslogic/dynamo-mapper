package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.DynamoMapper;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.test.EncodeDecode;
import com.autonomouslogic.dynamomapper.test.StdObjectMapper;
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
	DynamoMapper mapper = new DynamoMapper(StdObjectMapper.objectMapper());

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	public void shouldEncode(CodecTests test) {
		var ddb = mapper.encode(test.getPojo());
		assertEquals(test.getDdb(), ddb);
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values())
			.filter(CodecTests::encodeTest)
//			.filter(e -> e.name().equals("LIST_OBJECT"))
			.collect(Collectors.toList());
	}
}
