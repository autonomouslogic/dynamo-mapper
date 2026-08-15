// This is a generated file, do not edit manually.
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.model.MappedBatchGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedQueryResponse;
import com.autonomouslogic.dynamomapper.model.MappedScanResponse;
import com.autonomouslogic.dynamomapper.model.MappedUpdateItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ItemCollectionSizeLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ReplicatedWriteConflictException;
import software.amazon.awssdk.services.dynamodb.model.RequestLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ThrottlingException;
import software.amazon.awssdk.services.dynamodb.model.TransactionConflictException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class DynamoMapper {
	private final DynamoDbClient client;

	final DynamoEncoder encoder;

	final DynamoDecoder decoder;

	final RequestFactory requestFactory;

	final ReflectionUtil reflectionUtil;

	protected DynamoMapper(DynamoDbClient client, ObjectMapper objectMapper, TableNameDecorator tableNameDecorator) {
		this.client = client;
		reflectionUtil = new ReflectionUtil(objectMapper, tableNameDecorator);
		encoder = new DynamoEncoder(objectMapper, reflectionUtil);
		decoder = new DynamoDecoder(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> MappedGetItemResponse<T> getItem(
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		Consumer<GetItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptGetItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapGetItemResponse(client.getItem(reqOrConsumer), clazz);
	}

	public <T> MappedGetItemResponse<T> getItemFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getItemRequestFromPrimaryKey(primaryKey, clazz);
		consumer.accept(builder);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<GetItemRequest.Builder> consumer)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull GetItemRequest request, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptGetItemRequest(request, clazz);
		return decoder.mapGetItemResponse(client.getItem(reqOrConsumer), clazz);
	}

	public <T> MappedGetItemResponse<T> getItemFromPrimaryKey(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getItemRequestFromPrimaryKey(primaryKey, clazz);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItemFromKeyObject(@NonNull T keyObject)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getItemRequestFromKeyObject(keyObject);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedBatchGetItemResponse<T> batchGetItem(@NonNull BatchGetItemRequest request, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptBatchGetItemRequest(request, clazz);
		return decoder.mapBatchGetItemResponse(client.batchGetItem(reqOrConsumer), clazz);
	}

	public <T> MappedBatchGetItemResponse<T> batchGetItemFromPrimaryKeys(
			@NonNull List<?> primaryKey, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
		return batchGetItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedBatchGetItemResponse<T> batchGetItemFromKeyObjects(
			@NonNull List<T> keyObject, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		return batchGetItem(builder.build(), clazz);
	}

	public <T> MappedBatchGetItemResponse<T> batchGetItem(
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		Consumer<BatchGetItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptBatchGetItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapBatchGetItemResponse(client.batchGetItem(reqOrConsumer), clazz);
	}

	public <T> MappedBatchGetItemResponse<T> batchGetItemFromPrimaryKeys(
			@NonNull List<?> primaryKey,
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer,
			@NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.batchGetItemRequestFromPrimaryKeys(primaryKey, clazz);
		consumer.accept(builder);
		return batchGetItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedBatchGetItemResponse<T> batchGetItemFromKeyObjects(
			@NonNull List<T> keyObject,
			@NonNull Class<T> clazz,
			@NonNull Consumer<BatchGetItemRequest.Builder> consumer)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.batchGetItemRequestFromKeyObjects(keyObject);
		consumer.accept(builder);
		return batchGetItem(builder.build(), clazz);
	}

	public <T> MappedPutItemResponse<T> putItem(
			@NonNull Consumer<PutItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		Consumer<PutItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptPutItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapPutItemResponse(client.putItem(reqOrConsumer), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedPutItemResponse<T> putItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<PutItemRequest.Builder> consumer)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.putItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull PutItemRequest request, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptPutItemRequest(request, clazz);
		return decoder.mapPutItemResponse(client.putItem(reqOrConsumer), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedPutItemResponse<T> putItemFromKeyObject(@NonNull T keyObject)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.putItemRequestFromKeyObject(keyObject);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedUpdateItemResponse<T> updateItem(
			@NonNull Consumer<UpdateItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		Consumer<UpdateItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptUpdateItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapUpdateItemResponse(client.updateItem(reqOrConsumer), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedUpdateItemResponse<T> updateItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<UpdateItemRequest.Builder> consumer)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.updateItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return updateItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedUpdateItemResponse<T> updateItem(@NonNull UpdateItemRequest request, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptUpdateItemRequest(request, clazz);
		return decoder.mapUpdateItemResponse(client.updateItem(reqOrConsumer), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedUpdateItemResponse<T> updateItemFromKeyObject(@NonNull T keyObject)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.updateItemRequestFromKeyObject(keyObject);
		return updateItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		Consumer<DeleteItemRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptDeleteItemRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapDeleteItemResponse(client.deleteItem(reqOrConsumer), clazz);
	}

	public <T> MappedDeleteItemResponse<T> deleteItemFromPrimaryKey(
			@NonNull Object primaryKey, @NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.deleteItemRequestFromPrimaryKey(primaryKey, clazz);
		consumer.accept(builder);
		return deleteItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedDeleteItemResponse<T> deleteItemFromKeyObject(
			@NonNull T keyObject, @NonNull Consumer<DeleteItemRequest.Builder> consumer)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.deleteItemRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull DeleteItemRequest request, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptDeleteItemRequest(request, clazz);
		return decoder.mapDeleteItemResponse(client.deleteItem(reqOrConsumer), clazz);
	}

	public <T> MappedDeleteItemResponse<T> deleteItemFromPrimaryKey(@NonNull Object primaryKey, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.deleteItemRequestFromPrimaryKey(primaryKey, clazz);
		return deleteItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedDeleteItemResponse<T> deleteItemFromKeyObject(@NonNull T keyObject)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException,
					ItemCollectionSizeLimitExceededException, TransactionConflictException,
					RequestLimitExceededException, InternalServerErrorException, ReplicatedWriteConflictException,
					ThrottlingException, AwsServiceException, SdkClientException, DynamoDbException,
					JsonProcessingException, IOException {
		var builder = requestFactory.deleteItemRequestFromKeyObject(keyObject);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedScanResponse<T> scan(@NonNull Consumer<ScanRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		Consumer<ScanRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptScanRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapScanResponse(client.scan(reqOrConsumer), clazz);
	}

	public <T> MappedScanResponse<T> scan(@NonNull ScanRequest request, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptScanRequest(request, clazz);
		return decoder.mapScanResponse(client.scan(reqOrConsumer), clazz);
	}

	public <T> MappedQueryResponse<T> query(@NonNull Consumer<QueryRequest.Builder> consumer, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		Consumer<QueryRequest.Builder> reqOrConsumer = (builder) -> {
			{
				requestFactory.acceptQueryRequest(builder, clazz);
				consumer.accept(builder);
			}
		};
		return decoder.mapQueryResponse(client.query(reqOrConsumer), clazz);
	}

	public <T> MappedQueryResponse<T> query(@NonNull QueryRequest request, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
					InternalServerErrorException, ThrottlingException, AwsServiceException, SdkClientException,
					DynamoDbException, JsonProcessingException {
		var reqOrConsumer = requestFactory.acceptQueryRequest(request, clazz);
		return decoder.mapQueryResponse(client.query(reqOrConsumer), clazz);
	}

	public static DynamoMapperBuilder builder() {
		return new DynamoMapperBuilder();
	}
}
