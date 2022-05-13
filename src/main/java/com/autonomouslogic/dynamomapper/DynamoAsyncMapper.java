package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.CheckedFunction;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.util.FutureUtil;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DynamoAsyncMapper {
	private final DynamoDbAsyncClient client;
	private final DynamoEncoder encoder;
	private final DynamoDecoder decoder;
	private final RequestFactory requestFactory;
	private final ReflectionUtil reflectionUtil;

	public DynamoAsyncMapper(@NonNull DynamoDbAsyncClient client, @NonNull ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull Object hashKey, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
			return getItem(builder.build(), clazz);
		});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull Object hashKey, @NonNull Consumer<GetItemRequest.Builder> getItemRequest, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
			getItemRequest.accept(builder);
			return getItem(builder.build(), clazz);
		});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(GetItemRequest getItemRequest, @NonNull Class<T> clazz) {
		return client.getItem(getItemRequest)
			.thenApply(new CheckedFunction<>() {
				@Override
				public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
					return decoder.mapGetItemResponse(response, clazz);
				}
			});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull Consumer<GetItemRequest.Builder> getItemRequest, @NonNull Class<T> clazz) {
		return client.getItem(getItemRequest)
			.thenApply(new CheckedFunction<>() {
				@Override
				public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
					return decoder.mapGetItemResponse(response, clazz);
				}
			});
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull PutItemRequest putItemRequest, @NonNull Class<T> clazz) {
		return client.putItem(putItemRequest)
			.thenApply(new CheckedFunction<>() {
				@Override
				public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
					return decoder.mapPutItemResponse(response, clazz);
				}
			});
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull Consumer<PutItemRequest.Builder> putItemRequest, @NonNull Class<T> clazz) {
		return client.putItem(putItemRequest)
			.thenApply(new CheckedFunction<>() {
				@Override
				public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
					return decoder.mapPutItemResponse(response, clazz);
				}
			});
	}
}
