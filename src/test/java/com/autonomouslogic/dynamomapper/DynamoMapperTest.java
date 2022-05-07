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
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		expectedDdb = new HashMap<>();
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
		BOOL(
			new TestObject().setBool(true),
			Map.of("bool", AttributeValue.builder().bool(true).build())
		),
		BINARY_SET(
			new TestObject().setBinarySet(Set.of(
				new byte[] {1, 2, 3, 4},
				new byte[] {5, 6, 7, 8}
			)),
			Map.of("binarySet", AttributeValue.builder().bs(
				SdkBytes.fromByteArray(Base64.encodeBase64(new byte[] {1, 2, 3, 4})),
				SdkBytes.fromByteArray(Base64.encodeBase64(new byte[] {5, 6, 7, 8}))
			).build())
		),
		BINARY_LIST(
			new TestObject().setBinaryList(List.of(
				new byte[] {1, 2, 3, 4},
				new byte[] {5, 6, 7, 8}
			)),
			Map.of("binaryList", AttributeValue.builder().bs(
				SdkBytes.fromByteArray(Base64.encodeBase64(new byte[] {1, 2, 3, 4})),
				SdkBytes.fromByteArray(Base64.encodeBase64(new byte[] {5, 6, 7, 8}))
			).build())
		),
		LIST_STRING(
			new TestObject().setListString(List.of("str1", "str2")),
			Map.of("listString", AttributeValue.builder().l(
				AttributeValue.builder().s("str1").build(),
				AttributeValue.builder().s("str2").build()
			).build())
		),
		LIST_OBJECT(
			new TestObject().setListObject(List.of(
				new TestObject().setString("str1"),
				new TestObject().setString("str2")
			)),
			Map.of("listObject", AttributeValue.builder().l(
				AttributeValue.builder().m(
					Map.of("string", AttributeValue.builder().s("str1").build())
				).build(),
				AttributeValue.builder().m(
					Map.of("string", AttributeValue.builder().s("str2").build())
				).build()
			).build())
		),
		LONG(
			new TestObject().setNumber(100),
			Map.of("number", AttributeValue.builder().n("100").build())
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
