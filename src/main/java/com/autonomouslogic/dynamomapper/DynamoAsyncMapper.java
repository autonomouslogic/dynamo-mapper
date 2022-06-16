// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.CheckedFunction;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedQueryResponse;
import com.autonomouslogic.dynamomapper.model.MappedScanResponse;
import com.autonomouslogic.dynamomapper.model.MappedUpdateItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.FutureUtil;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<GetItemRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptGetItemRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.getItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(@NonNull GetItemRequest request,
			@NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptGetItemRequest(request, clazz);
		return client.getItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(
			@NonNull Consumer<PutItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<PutItemRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptPutItemRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.putItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(@NonNull PutItemRequest request,
			@NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptPutItemRequest(request, clazz);
		return client.putItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(
			@NonNull Consumer<UpdateItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<UpdateItemRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptUpdateItemRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.updateItem(reqOrConsumer)
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
		var reqOrConsumer = requestFactory.acceptUpdateItemRequest(request, clazz);
		return client.updateItem(reqOrConsumer)
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
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<DeleteItemRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptDeleteItemRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.deleteItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(
			@NonNull DeleteItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptDeleteItemRequest(request, clazz);
		return client.deleteItem(reqOrConsumer)
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

	public <T> CompletableFuture<MappedScanResponse<T>> scan(
			@NonNull Consumer<ScanRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<ScanRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptScanRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.scan(reqOrConsumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedScanResponse<T> checkedApply(ScanResponse response) throws Exception {
							return decoder.mapScanResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedScanResponse<T>> scan(@NonNull ScanRequest request,
			@NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptScanRequest(request, clazz);
		return client.scan(reqOrConsumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedScanResponse<T> checkedApply(ScanResponse response) throws Exception {
							return decoder.mapScanResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedQueryResponse<T>> query(
			@NonNull Consumer<QueryRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<QueryRequest.Builder> reqOrConsumer = (builder) -> { {
			requestFactory.acceptQueryRequest(builder, clazz);
			consumer.accept(builder);
		} };
		return client.query(reqOrConsumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedQueryResponse<T> checkedApply(QueryResponse response) throws Exception {
							return decoder.mapQueryResponse(response, clazz);
						}
					});
	}

	public <T> CompletableFuture<MappedQueryResponse<T>> query(@NonNull QueryRequest request,
			@NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptQueryRequest(request, clazz);
		return client.query(reqOrConsumer)
					.thenApply(new CheckedFunction<>() {
						@Override
						public MappedQueryResponse<T> checkedApply(QueryResponse response) throws Exception {
							return decoder.mapQueryResponse(response, clazz);
						}
					});
	}

	public static DynamoAsyncMapperBuilder builder() {
		return new DynamoAsyncMapperBuilder();
	}
}
