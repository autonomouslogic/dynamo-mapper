package com.autonomouslogic.dynamomapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

		expectedPojo = new TestObject()
			.setString("str-val");

		expectedDdb = new HashMap<String, AttributeValue>();
		expectedDdb.put("string", AttributeValue.builder().s("str-val").build());
	}

	@Test
	public void shouldEncodeValues() {
		var ddb = mapper.encode(expectedPojo);
		assertEquals(expectedDdb, ddb);
	}

	//@Test
	public void shouldDecodeValues() {
		var pojo = mapper.decode(expectedDdb);
		assertEquals(expectedPojo, pojo);
	}
}
