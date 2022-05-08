package com.autonomouslogic.dynamomapper.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@RequiredArgsConstructor
public class DynamoDecoder {
	@NonNull
	private final ObjectMapper objectMapper;

	/**
	 * Decodes DynamoDB values into a POJO.
	 */
	public <T> T decode(Map<String, AttributeValue> map, Class<T> clazz) throws JsonProcessingException {
		ObjectNode json = objectMapper.createObjectNode();
		for (Map.Entry<String, AttributeValue> entry : map.entrySet()) {
			json.set(entry.getKey(), decodeValue(entry.getValue()));
		}
		return objectMapper.treeToValue(json, clazz);
	}

	private JsonNode decodeValue(AttributeValue val) {
		var nodeFactory = objectMapper.getNodeFactory();
		if (val.b() != null) {
			return nodeFactory.binaryNode(val.b().asByteArray());
		}
		if (val.bool() != null) {
			return nodeFactory.booleanNode(val.bool());
		}
		if (val.nul() != null) {
			return nodeFactory.nullNode();
		}
		if (val.n() != null) {
			return nodeFactory.textNode(val.n());
		}
		throw new IllegalArgumentException();
	}
}
