// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.CheckedFunction;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedUpdateItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.FutureUtil;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.SuppressWarnings;
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
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

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

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull Object hashKey,
			@NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
			return getItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull T keyObject) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromKeyObject(keyObject);
			return getItem(builder.build(), (Class<T>) keyObject.getClass());
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

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull Object hashKey,
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
			consumer.accept(builder);
			return getItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull T keyObject,
			@NonNull Consumer<GetItemRequest.Builder> consumer) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.getRequestFromKeyObject(keyObject);
			consumer.accept(builder);
			return getItem(builder.build(), (Class<T>) keyObject.getClass());
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

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull T keyObject) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.putRequestFromObject(keyObject);
			return putItem(builder.build(), (Class<T>) keyObject.getClass());
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

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull T keyObject,
			@NonNull Consumer<PutItemRequest.Builder> consumer) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.putRequestFromObject(keyObject);
			consumer.accept(builder);
			return putItem(builder.build(), (Class<T>) keyObject.getClass());
		});
	}

	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(
			@NonNull Consumer<UpdateItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return client.updateItem(consumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedUpdateItemResponse<T> checkedApply(UpdateItemResponse response) throws Exception {
							return decoder.mapUpdateItemResponse(response, clazz);
						}
					});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(@NonNull T keyObject,
			@NonNull Consumer<UpdateItemRequest.Builder> consumer) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.updateRequestFromObject(keyObject);
			consumer.accept(builder);
			return updateItem(builder.build(), (Class<T>) keyObject.getClass());
		});
	}

	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(
			@NonNull UpdateItemRequest request, @NonNull Class<T> clazz) {
		return client.updateItem(request)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedUpdateItemResponse<T> checkedApply(UpdateItemResponse response) throws Exception {
							return decoder.mapUpdateItemResponse(response, clazz);
						}
					});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(@NonNull T keyObject) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.updateRequestFromObject(keyObject);
			return updateItem(builder.build(), (Class<T>) keyObject.getClass());
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

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(@NonNull Object hashKey,
			@NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
			return deleteItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(@NonNull T keyObject) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
			return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
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

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(@NonNull Object hashKey,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
			consumer.accept(builder);
			return deleteItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(@NonNull T keyObject,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
			consumer.accept(builder);
			return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
		});
	}

	public static DynamoAsyncMapperBuilder builder() {
		return new DynamoAsyncMapperBuilder();
	}
}
