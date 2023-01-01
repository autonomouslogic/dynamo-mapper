package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

@RequiredArgsConstructor
public class DynamoEncoder {
	@NonNull
	private final ObjectMapper objectMapper;

	@NonNull
	private final ReflectionUtil reflectionUtil;

	/**
	 * Encodes a POJO into DynamoDB values.
	 */
	public Map<String, AttributeValue> encode(@NonNull Object obj) throws IOException {
		var json = objectMapper.valueToTree(obj);
		if (json.isObject()) {
			return encodeObject(json);
		} else {
			throw new IllegalArgumentException(
					String.format("Can only encode objects, %s supplied", json.getNodeType()));
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

	public AttributeValue encodeValue(JsonNode node) throws IOException {
		if (node == null || node.isNull()) {
			return AttributeValue.builder().nul(true).build();
		}
		if (node.isBinary()) {
			return AttributeValue.builder()
					.b(SdkBytes.fromByteArrayUnsafe(node.binaryValue()))
					.build();
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

	private AttributeValue encodeList(@NonNull JsonNode list) throws IOException {
		var values = new ArrayList<AttributeValue>();
		for (var entry : list) {
			values.add(encodeValue(entry));
		}
		return AttributeValue.builder().l(values).build();
	}

	public <T> Map<String, AttributeValue> encodeKeyValue(@NonNull T keyObject) throws IOException {
		var encoded = encode(keyObject);
		var primaryKeys = reflectionUtil.resolvePrimaryKeyFields(keyObject.getClass());
		var keyValues = new HashMap<String, AttributeValue>();
		for (String field : primaryKeys) {
			keyValues.put(field, encoded.get(field));
		}
		return keyValues;
	}

	public <T> Map<String, AttributeValue> encodeKeyValue(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws IOException {
		var primaryKeys = reflectionUtil.resolvePrimaryKeyFields(clazz);
		if (primaryKeys.isEmpty()) {
			throw new IllegalArgumentException(String.format("No primary key defined on %s", clazz.getSimpleName()));
		}
		if (primaryKeys.size() > 1) {
			throw new IllegalArgumentException(
					String.format("Multiple primary keys defined on %s", clazz.getSimpleName()));
		}
		var json = objectMapper.valueToTree(primaryKey);
		var primaryKeyValue = encodeValue(json);
		return Map.of(primaryKeys.get(0), primaryKeyValue);
	}

	public Map<String, AttributeValueUpdate> encodeUpdates(@NonNull Object obj) throws IOException {
		var encoded = encode(obj);
		var key = encodeKeyValue(obj);
		for (String k : key.keySet()) {
			encoded.remove(k);
		}
		var updates = new HashMap<String, AttributeValueUpdate>();
		encoded.forEach((k, val) -> updates.put(
				k,
				AttributeValueUpdate.builder()
						.value(val)
						.action(AttributeAction.PUT)
						.build()));
		return updates;
	}
}
