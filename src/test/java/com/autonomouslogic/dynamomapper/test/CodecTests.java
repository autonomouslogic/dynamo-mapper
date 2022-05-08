package com.autonomouslogic.dynamomapper.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum CodecTests {
	BINARY(
		new TestObject().setBinary(new byte[]{1, 2, 3, 4}),
		Map.of("string", AttributeValue.builder()
			.b(SdkBytes.fromByteArray(Base64.encodeBase64(new byte[]{1, 2, 3, 4}))).build()),
		EncodeDecode.DECODE_ONLY
	),
	BOOL(
		new TestObject().setBool(true),
		Map.of("bool", AttributeValue.builder().bool(true).build())
	),
	BINARY_SET(
		new TestObject().setBinarySet(Set.of(
			new byte[]{1, 2, 3, 4},
			new byte[]{5, 6, 7, 8}
		)),
		Map.of("binarySet", AttributeValue.builder().bs(
			SdkBytes.fromByteArray(Base64.encodeBase64(new byte[]{1, 2, 3, 4})),
			SdkBytes.fromByteArray(Base64.encodeBase64(new byte[]{5, 6, 7, 8}))
		).build())
	),
	BINARY_LIST(
		new TestObject().setBinaryList(List.of(
			new byte[]{1, 2, 3, 4},
			new byte[]{5, 6, 7, 8}
		)),
		Map.of("binaryList", AttributeValue.builder().bs(
			SdkBytes.fromByteArray(Base64.encodeBase64(new byte[]{1, 2, 3, 4})),
			SdkBytes.fromByteArray(Base64.encodeBase64(new byte[]{5, 6, 7, 8}))
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
	INTEGER(
		new TestObject().setNumber(100),
		Map.of("number", AttributeValue.builder().n("100").build())
	),
	INTEGER_LIST(
		new TestObject().setNumberList(List.of(1, 2, 3)),
		Map.of("numberList", AttributeValue.builder().ns("1", "2", "3").build())
	),
	INTEGER_SET(
		new TestObject().setNumberSet(Set.of(1, 2, 3)),
		Map.of("numberSet", AttributeValue.builder().ns("1", "2", "3").build())
	),
	NULL(
		new TestObject(),
		Map.of("nul", AttributeValue.builder().nul(true).build()),
		EncodeDecode.DECODE_ONLY
	),
	STRING(
		new TestObject().setString("str-val"),
		Map.of("string", AttributeValue.builder().s("str-val").build())
	);

	TestObject pojo;
	Map<String, AttributeValue> ddb;
	EncodeDecode encodeDecode;

	CodecTests(TestObject pojo, Map<String, AttributeValue> ddb) {
		this(pojo, ddb, null);
	}

	public boolean encodeTest() {
		return encodeDecode == null || encodeDecode == EncodeDecode.ENCODE_ONLY;
	}

	public boolean decodeTest() {
		return encodeDecode == null || encodeDecode == EncodeDecode.DECODE_ONLY;
	}
}
