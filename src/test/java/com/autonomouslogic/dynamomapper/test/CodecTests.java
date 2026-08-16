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
			TestObject.builder().binary(BYTE_STRING_1).build(),
			Map.of(
					"binary",
					AttributeValue.builder()
							.b(SdkBytes.fromByteArray(BYTE_STRING_1))
							.build())),
	BOOL(
			TestObject.builder().bool(true).build(),
			Map.of("bool", AttributeValue.builder().bool(true).build())),
	BINARY_LIST(
			TestObject.builder()
					.binaryList(List.of(BYTE_STRING_1, BYTE_STRING_2))
					.build(),
			Map.of(
					"binaryList",
					AttributeValue.builder()
							.bs(SdkBytes.fromByteArray(BYTE_STRING_1), SdkBytes.fromByteArray(BYTE_STRING_2))
							.build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	LIST_STRING(
			TestObject.builder().listString(List.of("str1", "str2")).build(),
			Map.of(
					"listString",
					AttributeValue.builder()
							.l(
									AttributeValue.builder().s("str1").build(),
									AttributeValue.builder().s("str2").build())
							.build())),
	LIST_OBJECT(
			TestObject.builder()
					.listObject(List.of(
							TestObject.builder().string("str1").build(),
							TestObject.builder().string("str2").build()))
					.build(),
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
			TestObject.builder()
					.object(TestObject.builder().string("str1").build())
					.build(),
			Map.of(
					"object",
					AttributeValue.builder()
							.m(Map.of(
									"string", AttributeValue.builder().s("str1").build()))
							.build())),
	MAP_MAP(
			TestObject.builder().map(Map.of("k1", "v1", "k2", "v2")).build(),
			Map.of(
					"map",
					AttributeValue.builder()
							.m(Map.of(
									"k1", AttributeValue.builder().s("v1").build(),
									"k2", AttributeValue.builder().s("v2").build()))
							.build())),
	INTEGER(
			TestObject.builder().number(100).build(),
			Map.of("number", AttributeValue.builder().n("100").build())),
	INTEGER_TYPED(
			TestObject.builder().integer(100).build(),
			Map.of("integer", AttributeValue.builder().n("100").build())),
	LONG(
			TestObject.builder().longValue(100L).build(),
			Map.of("longValue", AttributeValue.builder().n("100").build())),
	LONG_OVERFLOW_INT(
			TestObject.builder().longValue(2147483648L).build(),
			Map.of("longValue", AttributeValue.builder().n("2147483648").build())),
	FLOAT(
			TestObject.builder().floatValue(1.5f).build(),
			Map.of("floatValue", AttributeValue.builder().n("1.5").build())),
	DOUBLE(
			TestObject.builder().doubleValue(1.5d).build(),
			Map.of("doubleValue", AttributeValue.builder().n("1.5").build())),
	DOUBLE_OVERFLOW_FLOAT(
			TestObject.builder().doubleValue(3.5E38d).build(),
			Map.of("doubleValue", AttributeValue.builder().n("3.5E38").build())),
	BIG_INTEGER(
			TestObject.builder()
					.bigInteger(new BigInteger("12345678901234567890"))
					.build(),
			Map.of(
					"bigInteger",
					AttributeValue.builder().n("12345678901234567890").build())),
	BIG_INTEGER_OVERFLOW_LONG(
			TestObject.builder()
					.bigInteger(new BigInteger("9223372036854775808"))
					.build(),
			Map.of(
					"bigInteger",
					AttributeValue.builder().n("9223372036854775808").build())),
	BIG_DECIMAL(
			TestObject.builder().bigDecimal(new BigDecimal("123.456")).build(),
			Map.of("bigDecimal", AttributeValue.builder().n("123.456").build())),
	BIG_DECIMAL_OVERFLOW_DOUBLE(
			TestObject.builder().bigDecimal(new BigDecimal("1.8E+308")).build(),
			Map.of("bigDecimal", AttributeValue.builder().n("1.8E+308").build())),
	INTEGER_LIST(
			TestObject.builder().numberList(List.of(1, 2, 3)).build(),
			Map.of("numberList", AttributeValue.builder().ns("1", "2", "3").build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	NULL(
			TestObject.builder().build(),
			Map.of("nul", AttributeValue.builder().nul(true).build()),
			EncodeDecode.DECODE_ONLY),
	STRING(
			TestObject.builder().string("str-val").build(),
			Map.of("string", AttributeValue.builder().s("str-val").build())),
	STRING_LIST(
			TestObject.builder().stringList(List.of("str1", "str2")).build(),
			Map.of("stringList", AttributeValue.builder().ss("str1", "str2").build()),
			EncodeDecode.DECODE_ONLY // @todo
			),
	BOOL_V1_FALSE(
			TestObject.builder().boolPrimitive(false).build(),
			Map.of("boolPrimitive", AttributeValue.builder().n("0").build()),
			EncodeDecode.DECODE_ONLY),
	BOOL_V1_TRUE(
			TestObject.builder().boolPrimitive(true).build(),
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
