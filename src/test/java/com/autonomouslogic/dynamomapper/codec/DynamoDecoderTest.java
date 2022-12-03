package com.autonomouslogic.dynamomapper.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.autonomouslogic.dynamomapper.model.TestObject;
import com.autonomouslogic.dynamomapper.test.CodecTests;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

public class DynamoDecoderTest {
	ObjectMapper objectMapper = StdObjectMapper.objectMapper();
	DynamoDecoder decoder = new DynamoDecoder(objectMapper);

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldDecode(CodecTests test) {
		var pojo = decoder.decode(test.ddb(), TestObject.class);
		var json = objectMapper.writeValueAsString(pojo);
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapGetItemResponse(CodecTests test) {
		var response = GetItemResponse.builder().item(test.ddb()).build();
		var mapped = decoder.mapGetItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.item());
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
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
		var json = objectMapper.writeValueAsString(mapped.items());
		var expectedJson = objectMapper.writeValueAsString(Map.of("test", List.of(test.pojo())));
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapPutItemResponse(CodecTests test) {
		var response = PutItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapPutItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.item());
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapUpdateItemResponse(CodecTests test) {
		var response = UpdateItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapUpdateItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.item());
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapDeleteItemResponse(CodecTests test) {
		var response = DeleteItemResponse.builder().attributes(test.ddb()).build();
		var mapped = decoder.mapDeleteItemResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.item());
		var expectedJson = objectMapper.writeValueAsString(test.pojo());
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapScanResponse(CodecTests test) {
		var response = ScanResponse.builder().items(List.of(test.ddb())).build();
		var mapped = decoder.mapScanResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.items());
		var expectedJson = objectMapper.writeValueAsString(List.of(test.pojo()));
		assertEquals(expectedJson, json);
	}

	@ParameterizedTest
	@MethodSource("loadTests")
	@SneakyThrows
	void shouldMapQueryResponse(CodecTests test) {
		var response = QueryResponse.builder().items(List.of(test.ddb())).build();
		var mapped = decoder.mapQueryResponse(response, TestObject.class);
		assertSame(response, mapped.response());
		var json = objectMapper.writeValueAsString(mapped.items());
		var expectedJson = objectMapper.writeValueAsString(List.of(test.pojo()));
		assertEquals(expectedJson, json);
	}

	public static List<CodecTests> loadTests() {
		return Stream.of(CodecTests.values()).filter(CodecTests::decodeTest).collect(Collectors.toList());
	}
}
