package com.autonomouslogic.dynamomapper.test;

import static com.autonomouslogic.dynamomapper.test.Util.BYTE_STRING_1;
import static com.autonomouslogic.dynamomapper.test.Util.BYTE_STRING_2;

import com.autonomouslogic.dynamomapper.model.TestObject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * @link https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_AttributeValue.html
 */
@AllArgsConstructor
@Getter
public enum CodecTests {
	BINARY(
			new TestObject().setBinary(BYTE_STRING_1),
			Map.of(
					"binary",
					AttributeValue.builder()
							.b(SdkBytes.fromByteArray(BYTE_STRING_1))
							.build())),
	BOOL(
			new TestObject().setBool(true),
			Map.of("bool", AttributeValue.builder().bool(true).build())),
	BINARY_LIST(
			new TestObject().setBinaryList(List.of(BYTE_STRING_1, BYTE_STRING_2)),
			Map.of(
					"binaryList",
					AttributeValue.builder()
							.bs(SdkBytes.fromByteArray(BYTE_STRING_1), SdkBytes.fromByteArray(BYTE_STRING_2))
							.build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	LIST_STRING(
			new TestObject().setListString(List.of("str1", "str2")),
			Map.of(
					"listString",
					AttributeValue.builder()
							.l(
									AttributeValue.builder().s("str1").build(),
									AttributeValue.builder().s("str2").build())
							.build())),
	LIST_OBJECT(
			new TestObject()
					.setListObject(List.of(new TestObject().setString("str1"), new TestObject().setString("str2"))),
			Map.of(
					"listObject",
					AttributeValue.builder()
							.l(
									AttributeValue.builder()
											.m(Map.of(
													"string",
													AttributeValue.builder()
															.s("str1")
															.build()))
											.build(),
									AttributeValue.builder()
											.m(Map.of(
													"string",
													AttributeValue.builder()
															.s("str2")
															.build()))
											.build())
							.build())),
	MAP_OBJECT(
			new TestObject().setObject(new TestObject().setString("str1")),
			Map.of(
					"object",
					AttributeValue.builder()
							.m(Map.of(
									"string", AttributeValue.builder().s("str1").build()))
							.build())),
	MAP_MAP(
			new TestObject().setMap(Map.of("k1", "v1", "k2", "v2")),
			Map.of(
					"map",
					AttributeValue.builder()
							.m(Map.of(
									"k1", AttributeValue.builder().s("v1").build(),
									"k2", AttributeValue.builder().s("v2").build()))
							.build())),
	INTEGER(
			new TestObject().setNumber(100),
			Map.of("number", AttributeValue.builder().n("100").build())),
	INTEGER_TYPED(
			new TestObject().setInteger(100),
			Map.of("integer", AttributeValue.builder().n("100").build())),
	LONG(
			new TestObject().setLongValue(100L),
			Map.of("longValue", AttributeValue.builder().n("100").build())),
	LONG_OVERFLOW_INT(
			new TestObject().setLongValue(2147483648L),
			Map.of("longValue", AttributeValue.builder().n("2147483648").build())),
	FLOAT(
			new TestObject().setFloatValue(1.5f),
			Map.of("floatValue", AttributeValue.builder().n("1.5").build())),
	DOUBLE(
			new TestObject().setDoubleValue(1.5d),
			Map.of("doubleValue", AttributeValue.builder().n("1.5").build())),
	DOUBLE_OVERFLOW_FLOAT(
			new TestObject().setDoubleValue(3.5E38d),
			Map.of("doubleValue", AttributeValue.builder().n("3.5E38").build())),
	BIG_INTEGER(
			new TestObject().setBigInteger(new BigInteger("12345678901234567890")),
			Map.of(
					"bigInteger",
					AttributeValue.builder().n("12345678901234567890").build())),
	BIG_INTEGER_OVERFLOW_LONG(
			new TestObject().setBigInteger(new BigInteger("9223372036854775808")),
			Map.of(
					"bigInteger",
					AttributeValue.builder().n("9223372036854775808").build())),
	BIG_DECIMAL(
			new TestObject().setBigDecimal(new BigDecimal("123.456")),
			Map.of("bigDecimal", AttributeValue.builder().n("123.456").build())),
	BIG_DECIMAL_OVERFLOW_DOUBLE(
			new TestObject().setBigDecimal(new BigDecimal("1.8E+308")),
			Map.of("bigDecimal", AttributeValue.builder().n("1.8E+308").build())),
	INTEGER_LIST(
			new TestObject().setNumberList(List.of(1, 2, 3)),
			Map.of("numberList", AttributeValue.builder().ns("1", "2", "3").build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	NULL(new TestObject(), Map.of("nul", AttributeValue.builder().nul(true).build()), EncodeDecode.DECODE_ONLY),
	STRING(
			new TestObject().setString("str-val"),
			Map.of("string", AttributeValue.builder().s("str-val").build())),
	STRING_LIST(
			new TestObject().setStringList(List.of("str1", "str2")),
			Map.of("stringList", AttributeValue.builder().ss("str1", "str2").build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	// v1 SDK stores boolean as N ("0"/"1") instead of BOOL.
	BOOL_V1_FALSE(
			new TestObject().setBoolPrimitive(false),
			Map.of("boolPrimitive", AttributeValue.builder().n("0").build()),
			EncodeDecode.DECODE_ONLY),
	BOOL_V1_TRUE(
			new TestObject().setBoolPrimitive(true),
			Map.of("boolPrimitive", AttributeValue.builder().n("1").build()),
			EncodeDecode.DECODE_ONLY);

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
