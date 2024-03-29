package com.autonomouslogic.dynamomapper.request;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@RequiredArgsConstructor
public class RequestFactory {
	private final DynamoEncoder encoder;
	private final ObjectMapper objectMapper;
	private final ReflectionUtil reflectionUtil;

	public <T> GetItemRequest.Builder getItemRequestFromPrimaryKey(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws IOException {
		return GetItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(clazz))
				.key(encoder.encodeKeyValue(primaryKey, clazz));
	}

	public <T> GetItemRequest.Builder getItemRequestFromKeyObject(@NonNull Object keyObject) throws IOException {
		return GetItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(keyObject.getClass()))
				.key(encoder.encodeKeyValue(keyObject));
	}

	public <T> BatchGetItemRequest.Builder batchGetItemRequestFromPrimaryKeys(
			@NonNull List<?> primaryKeys, @NonNull Class<T> clazz) throws IOException {
		var tableName = reflectionUtil.resolveTableName(clazz);
		var keyObjects = new ArrayList<Map<String, AttributeValue>>(primaryKeys.size());
		for (Object primaryKey : primaryKeys) {
			keyObjects.add(encoder.encodeKeyValue(primaryKey, clazz));
		}
		return BatchGetItemRequest.builder()
				.requestItems(Map.of(
						tableName, KeysAndAttributes.builder().keys(keyObjects).build()));
	}

	public <T> BatchGetItemRequest.Builder batchGetItemRequestFromKeyObjects(@NonNull List<T> keyObjects)
			throws IOException {
		Class<?> clazz = null;
		for (var k : keyObjects) {
			if (clazz == null) {
				clazz = k.getClass();
			} else if (clazz != k.getClass()) {
				throw new IllegalArgumentException(String.format(
						"Key objects must be the same class, expected %s, but %s seen", clazz, k.getClass()));
			}
		}
		var tableName = reflectionUtil.resolveTableName(clazz);
		var keys = new ArrayList<Map<String, AttributeValue>>(keyObjects.size());
		for (Object primaryKey : keyObjects) {
			var keyValue = encoder.encodeKeyValue(primaryKey);
			keys.add(keyValue);
		}
		return BatchGetItemRequest.builder()
				.requestItems(
						Map.of(tableName, KeysAndAttributes.builder().keys(keys).build()));
	}

	public PutItemRequest.Builder putItemRequestFromKeyObject(@NonNull Object obj) throws IOException {
		var encoded = encoder.encode(obj);
		return PutItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(obj.getClass()))
				.item(encoded);
	}

	public UpdateItemRequest.Builder updateItemRequestFromKeyObject(@NonNull Object obj) throws IOException {
		var key = encoder.encodeKeyValue(obj);
		var updates = encoder.encodeUpdates(obj);
		return UpdateItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(obj.getClass()))
				.key(key)
				.attributeUpdates(updates);
	}

	public <T> DeleteItemRequest.Builder deleteItemRequestFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Class<T> clazz) throws IOException {
		return DeleteItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(clazz))
				.key(encoder.encodeKeyValue(primaryKey, clazz));
	}

	public DeleteItemRequest.Builder deleteItemRequestFromKeyObject(@NonNull Object keyObject) throws IOException {
		return DeleteItemRequest.builder()
				.tableName(reflectionUtil.resolveTableName(keyObject.getClass()))
				.key(encoder.encodeKeyValue(keyObject));
	}

	public GetItemRequest acceptGetItemRequest(@NonNull GetItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptGetItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public GetItemRequest.Builder acceptGetItemRequest(GetItemRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public BatchGetItemRequest acceptBatchGetItemRequest(@NonNull BatchGetItemRequest req, @NonNull Class<?> clazz) {
		var items = req.requestItems();
		var types = items.size();
		if (types != 1) {
			throw new IllegalArgumentException(
					String.format("Exactly one class type expected, %s given: %s", types, items.keySet()));
		}
		return req;
	}

	public BatchGetItemRequest.Builder acceptBatchGetItemRequest(
			BatchGetItemRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req;
	}

	public PutItemRequest acceptPutItemRequest(@NonNull PutItemRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptPutItemRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public PutItemRequest.Builder acceptPutItemRequest(PutItemRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
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

	public ScanRequest.Builder acceptScanRequest(ScanRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}

	public QueryRequest acceptQueryRequest(@NonNull QueryRequest req, @NonNull Class<?> clazz) {
		if (req.tableName() == null) {
			return acceptQueryRequest(req.toBuilder(), clazz).build();
		}
		return req;
	}

	public QueryRequest.Builder acceptQueryRequest(QueryRequest.@NonNull Builder req, @NonNull Class<?> clazz) {
		return req.tableName(reflectionUtil.resolveTableName(clazz));
	}
}
