package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.StdJsonMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import tools.jackson.databind.json.JsonMapper;

public class DynamoDecoderTest {
	JsonMapper jsonMapper = StdJsonMapper.jsonMapper();
	DynamoDecoder decoder = new DynamoDecoder(jsonMapper);

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldDecode(CodecTests test) {
		var pojo = decoder.decode(test.ddb(), TestObject.class);
		var json = jsonMapper.writeValueAsString(pojo);
		var expectedJson = jsonMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapGetItemResponse(CodecTests test) {
		var response = GetItemResponse.builder().item(test.ddb()).build();
		var mapped = decoder.mapGetItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.item());
		var expectedJson = jsonMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapBatchGetItemResponse(CodecTests test) {
		var response = BatchGetItemResponse.builder()
				.responses(Map.of("test", List.of(test.ddb())))
				.build();
		var mapped = decoder.mapBatchGetItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.items());
		var expectedJson = jsonMapper.writeValueAsString(Map.of("test", List.of(test.pojo())));
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapPutItemResponse(CodecTests test) {
		var response = PutItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapPutItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.item());
		var expectedJson = jsonMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapUpdateItemResponse(CodecTests test) {
		var response = UpdateItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapUpdateItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.item());
		var expectedJson = jsonMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapDeleteItemResponse(CodecTests test) {
		var response = DeleteItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapDeleteItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.item());
		var expectedJson = jsonMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapScanResponse(CodecTests test) {
		var response = ScanResponse.builder().items(List.of(test.ddb())).build();
		var mapped = decoder.mapScanResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.items());
		var expectedJson = jsonMapper.writeValueAsString(List.of(test.pojo()));
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapQueryResponse(CodecTests test) {
		var response = QueryResponse.builder().items(List.of(test.ddb())).build();
		var mapped = decoder.mapQueryResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = jsonMapper.writeValueAsString(mapped.items());
		var expectedJson = jsonMapper.writeValueAsString(List.of(test.pojo()));
		assertEquals(expectedJson, json);
	}

	@Test
	@SneakyThrows
	void shouldMapGetItemResponseWithNoItem() {
		var response = GetItemResponse.builder().build();
		var mapped = decoder.mapGetItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.item());
	}

	@Test
	@SneakyThrows
	void shouldMapBatchGetItemResponseWithNoResponses() {
		var response = BatchGetItemResponse.builder().build();
		var mapped = decoder.mapBatchGetItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.items());
	}

	@Test
	@SneakyThrows
	void shouldMapPutItemResponseWithNoAttributes() {
		var response = PutItemResponse.builder().build();
		var mapped = decoder.mapPutItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.item());
	}

	@Test
	@SneakyThrows
	void shouldMapUpdateItemResponseWithNoAttributes() {
		var response = UpdateItemResponse.builder().build();
		var mapped = decoder.mapUpdateItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.item());
	}

	@Test
	@SneakyThrows
	void shouldMapDeleteItemResponseWithNoAttributes() {
		var response = DeleteItemResponse.builder().build();
		var mapped = decoder.mapDeleteItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.item());
	}

	@Test
	@SneakyThrows
	void shouldMapScanResponseWithEmptyItems() {
		var response = ScanResponse.builder().build();
		var mapped = decoder.mapScanResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertEquals(List.of(), mapped.items());
	}

	@Test
	@SneakyThrows
	void shouldMapQueryResponseWithEmptyItems() {
		var response = QueryResponse.builder().build();
		var mapped = decoder.mapQueryResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertEquals(List.of(), mapped.items());
	}

	@Test
	@SneakyThrows
	void shouldMapScanResponseWithNullItems() {
		var response = mock(ScanResponse.class);
		when(response.items()).thenReturn(null);
		var mapped = decoder.mapScanResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.items());
	}

	@Test
	@SneakyThrows
	void shouldMapQueryResponseWithNullItems() {
		var response = mock(QueryResponse.class);
		when(response.items()).thenReturn(null);
		var mapped = decoder.mapQueryResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		assertNull(mapped.items());
	}

	@Test
	@SneakyThrows
	void shouldThrowOnEmptyAttributeValue() {
		var emptyAttr = AttributeValue.builder().build();
		assertThrows(
				IllegalArgumentException.class, () -> decoder.decode(Map.of("string", emptyAttr), TestObject.class));
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values()).filter(CodecTests::decodeTest).collect(Collectors.toList());
	}

	// ===== decodeNumber: N decoding must be unchanged, and must not throw on the common decimal case =====

	/** An N that fits in a long still decodes to a long, including the 19-digit Long.MAX_VALUE boundary. */
	@ParameterizedTest
	@CsvSource({
		"1, 1",
		"-1, -1",
		"0, 0",
		"999999999999999999, 999999999999999999",
		"9223372036854775807, 9223372036854775807",
		"-9223372036854775808, -9223372036854775808"
	})
	@SneakyThrows
	void shouldDecodeIntegralNAsLong(String n, long expected) {
		var decoded =
				decoder.decode(Map.of("longValue", AttributeValue.builder().n(n).build()), TestObject.class);
		assertEquals(expected, decoded.longValue());
	}

	/**
	 * A decimal N decodes to the same value as before. These used to reach Long.parseLong and throw a
	 * NumberFormatException per attribute -- 11.6M of them in a 600s production profile -- which the catch
	 * then turned into decodeString. The pattern check now routes them straight to decodeString, so the
	 * decoded value is identical and no exception is constructed.
	 */
	@ParameterizedTest
	@CsvSource({"0.75, 0.75", "0.8333333, 0.8333333", "0.4, 0.4", "-0.5, -0.5", "1.0, 1.0"})
	@SneakyThrows
	void shouldDecodeDecimalNWithoutParsingItAsLong(String n, double expected) {
		var decoded = decoder.decode(
				Map.of("doubleValue", AttributeValue.builder().n(n).build()), TestObject.class);
		assertEquals(expected, decoded.doubleValue());
	}

	/** An N too large for a long keeps its previous route through decodeString. */
	@ParameterizedTest
	@CsvSource({"9223372036854775808", "99999999999999999999999999"})
	@SneakyThrows
	void shouldDecodeOverlongNAsBigInteger(String n) {
		var decoded = decoder.decode(
				Map.of("bigInteger", AttributeValue.builder().n(n).build()), TestObject.class);
		assertEquals(new java.math.BigInteger(n), decoded.bigInteger());
	}

	/** High-precision decimals keep full precision, as they did via decodeString. */
	@ParameterizedTest
	@CsvSource({"0.6013582", "123456789.123456789"})
	@SneakyThrows
	void shouldDecodeHighPrecisionNAsBigDecimal(String n) {
		var decoded = decoder.decode(
				Map.of("bigDecimal", AttributeValue.builder().n(n).build()), TestObject.class);
		assertEquals(new java.math.BigDecimal(n), decoded.bigDecimal());
	}
}
