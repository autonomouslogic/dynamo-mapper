package com.autonomouslogic.dynamomapper.request;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.reflect.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RequestFactory {
	private final DynamoEncoder encoder;
	private final ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;

	public <T> GetItemRequest.Builder getRequestFromHashKey(Object hashKey, Class<T> clazz) throws IOException {
		var hashKeys = reflectionUtil.resolveHashKeyFields(clazz);
		if (hashKeys.isEmpty()) {
			throw new IllegalArgumentException(String.format("No hash key defined on %s", clazz.getSimpleName()));
		}
		if (hashKeys.size() > 1) {
			throw new IllegalArgumentException(String.format("Multiple hash keys defined on %s", clazz.getSimpleName()));
		}
		var hashKeyValue = encoder.encodeValue(objectMapper.valueToTree(hashKey));
		return GetItemRequest.builder()
			.tableName("@todo")
			.key(Map.of(hashKeys.get(0), hashKeyValue));
	}

	public <T> GetItemRequest.Builder getRequestFromKeyObject(Object keyObject) throws IOException {
		var encoded = encoder.encode(keyObject);
		var hashKeys = reflectionUtil.resolveHashKeyFields(keyObject.getClass());
		var keyValues = new HashMap<String, AttributeValue>();
		for (String field : hashKeys) {
			keyValues.put(field, encoded.get(field));
		}
		return GetItemRequest.builder()
			.tableName("@todo")
			.key(keyValues);
	}
}
