package com.autonomouslogic.dynamomapper.request;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RequestFactory {
	private final DynamoEncoder encoder;
	private final ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;

	public <T> GetItemRequest.Builder getRequestFromHashKey(@NonNull Object hashKey, @NonNull Class<T> clazz) throws IOException {
		return GetItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(clazz))
			.key(createKeyValue(hashKey, clazz));
	}

	public <T> GetItemRequest.Builder getRequestFromKeyObject(@NonNull Object keyObject) throws IOException {
		return GetItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(keyObject.getClass()))
			.key(createKeyValue(keyObject));
	}

	public PutItemRequest.Builder putRequestFromObject(@NonNull Object obj) throws IOException {
		var encoded = encoder.encode(obj);
		return PutItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(obj.getClass()))
			.item(encoded);
	}

	public UpdateItemRequest.Builder updateRequestFromObject(@NonNull Object obj) throws IOException {
		var encoded = encoder.encode(obj);
		var key = createKeyValue(obj);
		for (String k : key.keySet()) {
			encoded.remove(k);
		}
		var updates = new HashMap<String, AttributeValueUpdate>();
		encoded.forEach((k, val) -> updates.put(k, AttributeValueUpdate.builder()
			.value(val)
			.action(AttributeAction.PUT)
			.build()));
		return UpdateItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(obj.getClass()))
			.key(key)
			.attributeUpdates(updates);
	}

	public <T> DeleteItemRequest.Builder deleteRequestFromHashKey(@NonNull Object hashKey, @NonNull Class<T> clazz) throws IOException {
		return DeleteItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(clazz))
			.key(createKeyValue(hashKey, clazz));
	}

	public DeleteItemRequest.Builder deleteRequestFromKeyObject(@NonNull Object keyObject) throws IOException {
		return DeleteItemRequest.builder()
			.tableName(reflectionUtil.resolveTableName(keyObject.getClass()))
			.key(createKeyValue(keyObject));
	}

	private <T> Map<String, AttributeValue> createKeyValue(@NonNull Object hashKey, @NonNull Class<T> clazz) throws IOException {
		var hashKeys = reflectionUtil.resolveHashKeyFields(clazz);
		if (hashKeys.isEmpty()) {
			throw new IllegalArgumentException(String.format("No hash key defined on %s", clazz.getSimpleName()));
		}
		if (hashKeys.size() > 1) {
			throw new IllegalArgumentException(String.format("Multiple hash keys defined on %s", clazz.getSimpleName()));
		}
		var hashKeyValue = encoder.encodeValue(objectMapper.valueToTree(hashKey));
		return Map.of(hashKeys.get(0), hashKeyValue);
	}

	private <T> Map<String, AttributeValue> createKeyValue(@NonNull T keyObject) throws IOException {
		var encoded = encoder.encode(keyObject);
		var hashKeys = reflectionUtil.resolveHashKeyFields(keyObject.getClass());
		var keyValues = new HashMap<String, AttributeValue>();
		for (String field : hashKeys) {
			keyValues.put(field, encoded.get(field));
		}
		return keyValues;
	}
}
