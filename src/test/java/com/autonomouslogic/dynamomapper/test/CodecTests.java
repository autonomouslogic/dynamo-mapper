package com.autonomouslogic.dynamomapper.test;

import com.autonomouslogic.dynamomapper.model.TestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

import static com.autonomouslogic.dynamomapper.test.Util.BYTE_STRING_1;
import static com.autonomouslogic.dynamomapper.test.Util.BYTE_STRING_2;

/**
 * @link https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
 */
@AllArgsConstructor
@Getter
public enum CodecTests {
	BINARY(
		new TestObject().setBinary(BYTE_STRING_1),
		Map.of("binary", AttributeValue.builder()
			.b(SdkBytes.fromByteArray(BYTE_STRING_1)).build())
	),
	BOOL(
		new TestObject().setBool(true),
		Map.of("bool", AttributeValue.builder().bool(true).build())
	),
	BINARY_LIST(
		new TestObject().setBinaryList(List.of(
			BYTE_STRING_1,
			BYTE_STRING_2
		)),
		Map.of("binaryList", AttributeValue.builder().bs(
			SdkBytes.fromByteArray(BYTE_STRING_1),
			SdkBytes.fromByteArray(BYTE_STRING_2)
		).build()),
		EncodeDecode.DECODE_ONLY // @todo
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
	MAP_OBJECT(
		new TestObject().setObject(
			new TestObject().setString("str1")
		),
		Map.of("object", AttributeValue.builder().m(
			Map.of("string", AttributeValue.builder().s("str1").build())
		).build())
	),
	MAP_MAP(
		new TestObject().setMap(
			Map.of("k1", "v1", "k2", "v2")
		),
		Map.of("map", AttributeValue.builder().m(
			Map.of(
				"k1", AttributeValue.builder().s("v1").build(),
				"k2", AttributeValue.builder().s("v2").build()
			)
		).build())
	),
	INTEGER(
		new TestObject().setNumber(100),
		Map.of("number", AttributeValue.builder().n("100").build())
	),
	INTEGER_LIST(
		new TestObject().setNumberList(List.of(1, 2, 3)),
		Map.of("numberList", AttributeValue.builder().ns("1", "2", "3").build()),
		EncodeDecode.DECODE_ONLY // @todo
	),
	NULL(
		new TestObject(),
		Map.of("nul", AttributeValue.builder().nul(true).build()),
		EncodeDecode.DECODE_ONLY
	),
	STRING(
		new TestObject().setString("str-val"),
		Map.of("string", AttributeValue.builder().s("str-val").build())
	),
	STRING_LIST(
		new TestObject().setStringList(List.of("str1", "str2")),
		Map.of("stringList", AttributeValue.builder().ss("str1", "str2").build()),
		EncodeDecode.DECODE_ONLY // @todo
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
