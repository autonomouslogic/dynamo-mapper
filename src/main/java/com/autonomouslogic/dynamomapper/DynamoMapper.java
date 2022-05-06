package com.autonomouslogic.dynamomapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DynamoMapper {
	@NonNull
	private final ObjectMapper objectMapper;

	/**
	 * Encodes a POJO into DynamoDB values.
	 */
	public Map<String, AttributeValue> encode(@NonNull Object obj) {
		var json = objectMapper.valueToTree(obj);
		return encode(json);
	}

	private Map<String, AttributeValue> encode(@NonNull JsonNode json) {
		if (json.isObject()) {
			return encodeObject(json);
		}
		throw new IllegalArgumentException(String.format("Can only encode objects, %s supplied", json.getNodeType()));
	}

	private Map<String, AttributeValue> encodeObject(@NonNull JsonNode node) {
		var map = new HashMap<String, AttributeValue>();
		node.fields().forEachRemaining(field -> {
			map.put(field.getKey(), encodePrimitive(field.getValue()));
		});
		return map;
	}

	private AttributeValue encodeArray(@NonNull JsonNode array) {
		throw new UnsupportedOperationException();
	}

	private AttributeValue encodePrimitive(JsonNode node) {
		if (node.isTextual()) {
			return AttributeValue.builder().s(node.textValue()).build();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Decodes DynamoDB values into a POJO.
	 */
	public <T> T decode(Map<String, AttributeValue> map) {
		return null;
	}
}
