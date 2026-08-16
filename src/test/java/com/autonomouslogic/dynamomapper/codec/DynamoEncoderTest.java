package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.util.StdJsonMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.json.JsonMapper;

public class DynamoEncoderTest {
	JsonMapper jsonMapper = StdJsonMapper.jsonMapper();
	ReflectionUtil reflectionUtil = new ReflectionUtil(jsonMapper, null);
	DynamoEncoder encoder = new DynamoEncoder(StdJsonMapper.jsonMapper(), reflectionUtil);

	@DynamoTableName("no-key-table")
	@Value
	@Builder
	@Jacksonized
	static class NoKeyObject {
		@JsonProperty
		String field;
	}

	@DynamoTableName("multi-key-table")
	@Value
	@Builder
	@Jacksonized
	static class MultiKeyObject {
		@JsonProperty
		@DynamoPrimaryKey
		String key1;

		@JsonProperty
		@DynamoPrimaryKey
		String key2;
	}

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

	@Test
	@SneakyThrows
	void shouldRejectEncodeOfNonObject() {
		assertThrows(IllegalArgumentException.class, () -> encoder.encode("not-an-object"));
	}

	@Test
	@SneakyThrows
	void shouldRejectEncodeKeyValueWithNoPrimaryKey() {
		assertThrows(IllegalArgumentException.class, () -> encoder.encodeKeyValue("key1", NoKeyObject.class));
	}

	@Test
	@SneakyThrows
	void shouldRejectEncodeKeyValueWithMultiplePrimaryKeys() {
		assertThrows(IllegalArgumentException.class, () -> encoder.encodeKeyValue("key1", MultiKeyObject.class));
	}

	@Test
	@SneakyThrows
	void shouldRejectEncodeValueWithUnsupportedNodeType() {
		var missingNode = jsonMapper.missingNode();
		assertThrows(IllegalArgumentException.class, () -> encoder.encodeValue(missingNode));
	}
}
