package com.autonomouslogic.dynamomapper.request;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@RequiredArgsConstructor
public class RequestFactory {
	private final DynamoEncoder encoder;
	private final ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;

	public <T> GetItemRequest.Builder getRequestFromPrimaryKey(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws IOException {
		return GetItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(clazz))
				.key(createKeyValue(primaryKey, clazz));
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
		encoded.forEach((k, val) -> updates.put(
				k,
				AttributeValueUpdate.builder()
						.value(val)
						.action(AttributeAction.PUT)
						.build()));
		return UpdateItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(obj.getClass()))
				.key(key)
				.attributeUpdates(updates);
	}

	public <T> DeleteItemRequest.Builder deleteRequestFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Class<T> clazz) throws IOException {
		return DeleteItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(clazz))
				.key(createKeyValue(primaryKey, clazz));
	}

	public DeleteItemRequest.Builder deleteRequestFromKeyObject(@NonNull Object keyObject) throws IOException {
		return DeleteItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(keyObject.getClass()))
				.key(createKeyValue(keyObject));
	}

	private <T> Map<String, AttributeValue> createKeyValue(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws IOException {
		var primaryKeys = reflectionUtil.resolvePrimaryKeyFields(clazz);
		if (primaryKeys.isEmpty()) {
			throw new IllegalArgumentException(String.format("No hash key defined on %s", clazz.getSimpleName()));
		}
		if (primaryKeys.size() > 1) {
			throw new IllegalArgumentException(
					String.format("Multiple hash keys defined on %s", clazz.getSimpleName()));
		}
		var primaryKeyValue = encoder.encodeValue(objectMapper.valueToTree(primaryKey));
		return Map.of(primaryKeys.get(0), primaryKeyValue);
	}

	private <T> Map<String, AttributeValue> createKeyValue(@NonNull T keyObject) throws IOException {
		var encoded = encoder.encode(keyObject);
		var primaryKeys = reflectionUtil.resolvePrimaryKeyFields(keyObject.getClass());
		var keyValues = new HashMap<String, AttributeValue>();
		for (String field : primaryKeys) {
			keyValues.put(field, encoded.get(field));
		}
		return keyValues;
	}

	public GetItemRequest acceptGetItemRequest(@NonNull GetItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptGetItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public GetItemRequest.Builder acceptGetItemRequest(@NonNull GetItemRequest.Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public PutItemRequest acceptPutItemRequest(@NonNull PutItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptPutItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public PutItemRequest.Builder acceptPutItemRequest(@NonNull PutItemRequest.Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public UpdateItemRequest acceptUpdateItemRequest(@NonNull UpdateItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptUpdateItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public UpdateItemRequest.Builder acceptUpdateItemRequest(
			UpdateItemRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public DeleteItemRequest acceptDeleteItemRequest(@NonNull DeleteItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptDeleteItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public DeleteItemRequest.Builder acceptDeleteItemRequest(
			DeleteItemRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public ScanRequest acceptScanRequest(@NonNull ScanRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptScanRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public ScanRequest.Builder acceptScanRequest(@NonNull ScanRequest.Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public QueryRequest acceptQueryRequest(@NonNull QueryRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptQueryRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public QueryRequest.Builder acceptQueryRequest(@NonNull QueryRequest.Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}
}
