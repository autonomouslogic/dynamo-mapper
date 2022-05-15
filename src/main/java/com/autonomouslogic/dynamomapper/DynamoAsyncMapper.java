// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.CheckedFunction;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Class;
import java.lang.Exception;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

public class DynamoAsyncMapper {
	private final DynamoDbAsyncClient client;

	final DynamoEncoder encoder;

	final DynamoDecoder decoder;

	final RequestFactory requestFactory;

	final ReflectionUtil reflectionUtil;

	protected DynamoAsyncMapper(DynamoDbAsyncClient client, ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull GetItemRequest request,
			@NonNull Class<T> clazz) {
		return client.getItem(request)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
							return decoder.mapGetItemResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return client.getItem(consumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
							return decoder.mapGetItemResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull PutItemRequest request,
			@NonNull Class<T> clazz) {
		return client.putItem(request)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
							return decoder.mapPutItemResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(
			@NonNull Consumer<PutItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return client.putItem(consumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
							return decoder.mapPutItemResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(
			@NonNull DeleteItemRequest request, @NonNull Class<T> clazz) {
		return client.deleteItem(request)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedDeleteItemResponse<T> checkedApply(DeleteItemResponse response) throws Exception {
							return decoder.mapDeleteItemResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return client.deleteItem(consumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedDeleteItemResponse<T> checkedApply(DeleteItemResponse response) throws Exception {
							return decoder.mapDeleteItemResponse(response, clazz);
						}
					});
	}

	public static DynamoAsyncMapperBuilder builder() {
		return new DynamoAsyncMapperBuilder();
	}
}
