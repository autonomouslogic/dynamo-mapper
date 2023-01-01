// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.CheckedFunction;
import com.autonomouslogic.dynamomapper.model.MappedBatchGetItemResponse;
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
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
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

public class DynamoAsyncMapper {
	private final DynamoDbAsyncClient client;

	final DynamoEncoder encoder;

	final DynamoDecoder decoder;

	final RequestFactory requestFactory;

	final ReflectionUtil reflectionUtil;

	protected DynamoAsyncMapper(DynamoDbAsyncClient client, ObjectMapper objectMapper) {
		this.client = client;
		reflectionUtil = new ReflectionUtil(objectMapper);
		encoder = new DynamoEncoder(objectMapper, reflectionUtil);
		decoder = new DynamoDecoder(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<GetItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptGetItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.getItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
				return decoder.mapGetItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItemFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws IOException {
		var builder = requestFactory.getItemRequestFromPrimaryKey(primaryKey, clazz);
		consumer.accept(builder);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedGetItemResponse<T>> getItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<GetItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.getItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItem(
			@NonNull GetItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptGetItemRequest(request, clazz);
		return client.getItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedGetItemResponse<T> checkedApply(GetItemResponse response) throws Exception {
				return decoder.mapGetItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedGetItemResponse<T>> getItemFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Class<T> clazz) throws IOException {
		var builder = requestFactory.getItemRequestFromPrimaryKey(primaryKey, clazz);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedGetItemResponse<T>> getItemFromKeyObject(@NonNull T keyObject)
			throws IOException {
		var builder = requestFactory.getItemRequestFromKeyObject(keyObject);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItem(
			@NonNull BatchGetItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptBatchGetItemRequest(request, clazz);
		return client.batchGetItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedBatchGetItemResponse<T> checkedApply(BatchGetItemResponse response) throws Exception {
				return decoder.mapBatchGetItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItemFromPrimaryKeys(
			@NonNull List<?> primaryKey, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
			return batchGetItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItemFromKeyObjects(@NonNull List<?> keyObject)
			throws IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		return batchGetItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItem(
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<BatchGetItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptBatchGetItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.batchGetItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedBatchGetItemResponse<T> checkedApply(BatchGetItemResponse response) throws Exception {
				return decoder.mapBatchGetItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItemFromPrimaryKeys(
			@NonNull List<?> primaryKey,
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer,
			@NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
			consumer.accept(builder);
			return batchGetItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedBatchGetItemResponse<T>> batchGetItemFromKeyObjects(
			@NonNull List<?> keyObject, @NonNull Consumer<BatchGetItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		consumer.accept(builder);
		return batchGetItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(
			@NonNull Consumer<PutItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<PutItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptPutItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.putItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
				return decoder.mapPutItemResponse(response, clazz);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedPutItemResponse<T>> putItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<PutItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.putItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedPutItemResponse<T>> putItem(
			@NonNull PutItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptPutItemRequest(request, clazz);
		return client.putItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedPutItemResponse<T> checkedApply(PutItemResponse response) throws Exception {
				return decoder.mapPutItemResponse(response, clazz);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedPutItemResponse<T>> putItemFromKeyObject(@NonNull T keyObject)
			throws IOException {
		var builder = requestFactory.putItemRequestFromKeyObject(keyObject);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(
			@NonNull Consumer<UpdateItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<UpdateItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptUpdateItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.updateItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedUpdateItemResponse<T> checkedApply(UpdateItemResponse response) throws Exception {
				return decoder.mapUpdateItemResponse(response, clazz);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<UpdateItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.updateItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return updateItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItem(
			@NonNull UpdateItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptUpdateItemRequest(request, clazz);
		return client.updateItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedUpdateItemResponse<T> checkedApply(UpdateItemResponse response) throws Exception {
				return decoder.mapUpdateItemResponse(response, clazz);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedUpdateItemResponse<T>> updateItemFromKeyObject(@NonNull T keyObject)
			throws IOException {
		var builder = requestFactory.updateItemRequestFromKeyObject(keyObject);
		return updateItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<DeleteItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptDeleteItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.deleteItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedDeleteItemResponse<T> checkedApply(DeleteItemResponse response) throws Exception {
				return decoder.mapDeleteItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItemFromPrimaryKey(
			@NonNull Object primaryKey,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer,
			@NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteItemRequestFromPrimaryKey(primaryKey, clazz);
			consumer.accept(builder);
			return deleteItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<DeleteItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.deleteItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItem(
			@NonNull DeleteItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptDeleteItemRequest(request, clazz);
		return client.deleteItem(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedDeleteItemResponse<T> checkedApply(DeleteItemResponse response) throws Exception {
				return decoder.mapDeleteItemResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItemFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Class<T> clazz) {
		return FutureUtil.wrapFuture(() -> {
			var builder = requestFactory.deleteItemRequestFromPrimaryKey(primaryKey, clazz);
			return deleteItem(builder.build(), clazz);
		});
	}

	@SuppressWarnings("unchecked")
	public <T> CompletableFuture<MappedDeleteItemResponse<T>> deleteItemFromKeyObject(@NonNull T keyObject)
			throws IOException {
		var builder = requestFactory.deleteItemRequestFromKeyObject(keyObject);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> CompletableFuture<MappedScanResponse<T>> scan(
			@NonNull Consumer<ScanRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<ScanRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptScanRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.scan(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedScanResponse<T> checkedApply(ScanResponse response) throws Exception {
				return decoder.mapScanResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedScanResponse<T>> scan(@NonNull ScanRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptScanRequest(request, clazz);
		return client.scan(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedScanResponse<T> checkedApply(ScanResponse response) throws Exception {
				return decoder.mapScanResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedQueryResponse<T>> query(
			@NonNull Consumer<QueryRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<QueryRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptQueryRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.query(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedQueryResponse<T> checkedApply(QueryResponse response) throws Exception {
				return decoder.mapQueryResponse(response, clazz);
			}
		});
	}

	public <T> CompletableFuture<MappedQueryResponse<T>> query(@NonNull QueryRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptQueryRequest(request, clazz);
		return client.query(reqOrConsumer).thenApply(new CheckedFunction<>() {
			@Override
			public MappedQueryResponse<T> checkedApply(QueryResponse response) throws Exception {
				return decoder.mapQueryResponse(response, clazz);
			}
		});
	}

	public static DynamoAsyncMapperBuilder builder() {
		return new DynamoAsyncMapperBuilder();
	}

	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginator(
			@NonNull BatchGetItemRequest request, @NonNull Class<T> clazz) {
		var reqOrConsumer = requestFactory.acceptBatchGetItemRequest(request, clazz);
		return client.batchGetItemPaginator(reqOrConsumer).map(new CheckedFunction<>() {
			@Override
			public MappedBatchGetItemResponse<T> checkedApply(BatchGetItemResponse response) throws Exception {
				return decoder.mapBatchGetItemResponse(response, clazz);
			}
		});
	}

	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginatorFromPrimaryKeys(
			@NonNull List<?> primaryKey, @NonNull Class<T> clazz) throws IOException {
		var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
		return batchGetItemPaginator(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginatorFromKeyObjects(@NonNull List<?> keyObject)
			throws IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		return batchGetItemPaginator(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginator(
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer, @NonNull Class<T> clazz) {
		Consumer<BatchGetItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptBatchGetItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return client.batchGetItemPaginator(reqOrConsumer).map(new CheckedFunction<>() {
			@Override
			public MappedBatchGetItemResponse<T> checkedApply(BatchGetItemResponse response) throws Exception {
				return decoder.mapBatchGetItemResponse(response, clazz);
			}
		});
	}

	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginatorFromPrimaryKeys(
			@NonNull List<?> primaryKey,
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer,
			@NonNull Class<T> clazz)
			throws IOException {
		var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
		consumer.accept(builder);
		return batchGetItemPaginator(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> Publisher<MappedBatchGetItemResponse<T>> batchGetItemPaginatorFromKeyObjects(
			@NonNull List<?> keyObject, @NonNull Consumer<BatchGetItemRequest.Builder> consumer) throws IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		consumer.accept(builder);
		return batchGetItemPaginator(builder.build(), (Class<T>) keyObject.getClass());
	}
}
