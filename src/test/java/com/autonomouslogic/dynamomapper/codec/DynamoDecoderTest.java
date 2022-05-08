package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.DynamoMapper;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.test.EncodeDecode;
import com.autonomouslogic.dynamomapper.test.StdObjectMapper;
import com.autonomouslogic.dynamomapper.test.TestObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoDecoderTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	DynamoMapper mapper = new DynamoMapper(objectMapper);

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	public void shouldDecode(CodecTests test) {
		var pojo = mapper.decode(test.getDdb(), TestObject.class);
		var json = objectMapper.writeValueAsString(pojo);
		var expectedJson = objectMapper.writeValueAsString(test.getPojo());
		if (test.name().contains("BINARY")) {
			assertEquals(expectedJson, json);
		}
		else {
			assertEquals(test.getPojo(), pojo);
		}
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values())
			.filter(CodecTests::decodeTest)
			.collect(Collectors.toList());
	}
}
