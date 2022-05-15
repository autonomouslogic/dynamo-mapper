package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoEncoderTest {
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
}
