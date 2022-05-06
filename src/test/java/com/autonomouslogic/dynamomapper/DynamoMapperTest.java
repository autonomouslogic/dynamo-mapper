package com.autonomouslogic.dynamomapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoMapperTest {
	DynamoMapper mapper;
	Map<String, AttributeValue> expectedDdb;
	TestObject expectedPojo;

	@BeforeEach
	public void beforeEach() {
		var objectMapper = new ObjectMapper();
		mapper = new DynamoMapper(objectMapper);

		var bytes = new byte[] {1, 2, 3};

		expectedPojo = new TestObject()
			.setBinary(bytes)
			.setString("str-val");

		expectedDdb = new HashMap<String, AttributeValue>();
		expectedDdb.put("binary", AttributeValue.builder().b(SdkBytes.fromByteArray(Base64.encodeBase64(bytes))).build());
//		expectedDdb.put("bool", AttributeValue.builder().bool(true).build()); @todo
//		expectedDdb.put("binarySet", AttributeValue.builder().bs().build()); @todo
//		expectedDdb.put("list", AttributeValue.builder().l().build()); @todo
//		expectedDdb.put("map", AttributeValue.builder().m().build()); @todo
//		expectedDdb.put("number", AttributeValue.builder().n().build()); @todo
//		expectedDdb.put("nul", AttributeValue.builder().nul(true).build()); @todo
		expectedDdb.put("string", AttributeValue.builder().s("str-val").build());
//		expectedDdb.put("stringSet", AttributeValue.builder().ss().build()); @todo
	}

	enum EncodeDecode { // @todo move to method source
		ENCODE_ONLY,
		DECODE_ONLY
	}

	@AllArgsConstructor
	@Getter
	enum Tests {
		BINARY(
			new TestObject().setBinary(new byte[] {1, 2, 3, 4}),
			Map.of("string", AttributeValue.builder()
				.b(SdkBytes.fromByteArray(Base64.encodeBase64(new byte[] {1, 2, 3, 4}))).build()),
			EncodeDecode.DECODE_ONLY
		),
		STRING(
			new TestObject().setString("str-val"),
			Map.of("string", AttributeValue.builder().s("str-val").build())
		);

		TestObject pojo;
		Map<String, AttributeValue> ddb;
		EncodeDecode encodeDecode;

		Tests(TestObject pojo, Map<String, AttributeValue> ddb) {
			this(pojo, ddb, null);
		}
	}

	@ParameterizedTest
	@EnumSource(Tests.class)
	public void shouldEncode(Tests test) {
		if (test.getEncodeDecode() == EncodeDecode.DECODE_ONLY) {
			return;
		}
		var ddb = mapper.encode(test.getPojo());
		assertEquals(test.getDdb(), ddb);
	}

	@ParameterizedTest
	@EnumSource(Tests.class)
	public void shouldDecode(Tests test) {
		if (test.getEncodeDecode() == EncodeDecode.ENCODE_ONLY) {
			return;
		}
		var pojo = mapper.decode(test.getDdb());
		assertEquals(test.getPojo(), pojo);
	}
}
