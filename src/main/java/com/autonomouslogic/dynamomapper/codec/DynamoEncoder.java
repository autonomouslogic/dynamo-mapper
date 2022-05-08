package com.autonomouslogic.dynamomapper.codec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DynamoEncoder {
	@NonNull
	private final ObjectMapper objectMapper;

	/**
	 * Encodes a POJO into DynamoDB values.
	 */
	public Map<String, AttributeValue> encode(@NonNull Object obj) throws IOException {
		var json = objectMapper.valueToTree(obj);
		if (json.isObject()) {
			return encodeObject(json);
		}
		else {
			throw new IllegalArgumentException(String.format("Can only encode objects, %s supplied", json.getNodeType()));
		}
	}

	private Map<String, AttributeValue> encodeObject(@NonNull JsonNode node) throws IOException {
		var map = new HashMap<String, AttributeValue>();
		var fieldsIterator = node.fields();
		while (fieldsIterator.hasNext()) {
			var field = fieldsIterator.next();
			map.put(field.getKey(), encodeValue(field.getValue()));
		}
		return map;
	}

	private AttributeValue encodeValue(JsonNode node) throws IOException {
		if (node == null || node.isNull()) {
			return AttributeValue.builder().nul(true).build();
		}
		if (node.isBinary()) {
			return AttributeValue.builder().b(SdkBytes.fromByteArray(node.binaryValue())).build();
		}
		if (node.isBoolean()) {
			return AttributeValue.builder().bool(node.booleanValue()).build();
		}
		if (node.isTextual()) {
			return AttributeValue.builder().s(node.textValue()).build();
		}
		if (node.isNumber()) {
			return AttributeValue.builder().n(node.numberValue().toString()).build();
		}
		if (node.isObject()) {
			var obj = encodeObject(node);
			return AttributeValue.builder().m(obj).build();
		}
		if (node.isArray()) {
			return encodeList(node);
		}
		throw new IllegalArgumentException(String.format("Unsupported node type: %s", node.getNodeType()));
	}

	private AttributeValue encodeList(@NonNull JsonNode list) {
		throw new UnsupportedOperationException();
	}

	private AttributeValue encodePrimitive(JsonNode node) {
		if (node.isTextual()) {
			return AttributeValue.builder().s(node.textValue()).build();
		}
		throw new IllegalArgumentException();
	}
}
