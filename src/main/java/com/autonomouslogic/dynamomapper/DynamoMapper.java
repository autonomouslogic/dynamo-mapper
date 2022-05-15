/*
 This is a generated file, do not edit manually.
*/
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.Class;
import java.lang.Object;
import java.lang.SuppressWarnings;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ItemCollectionSizeLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.RequestLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.TransactionConflictException;

class DynamoMapper {
	final DynamoDbClient client;

	final DynamoEncoder encoder;

	final DynamoDecoder decoder;

	final RequestFactory requestFactory;

	final ReflectionUtil reflectionUtil;

	public DynamoMapper(DynamoDbClient client, ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull GetItemRequest request,
			@NonNull Class<T> clazz) throws ProvisionedThroughputExceededException,
			ResourceNotFoundException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(request), clazz);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Object hashKey, @NonNull Class<T> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItem(@NonNull Object keyObject) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Consumer<GetItemRequest.Builder> consumer,
			@NonNull Class<T> clazz) throws ProvisionedThroughputExceededException,
			ResourceNotFoundException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(consumer), clazz);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Object hashKey,
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<T> clazz) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItem(@NonNull Object keyObject,
			@NonNull Consumer<GetItemRequest.Builder> consumer) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull Consumer<PutItemRequest.Builder> consumer,
			@NonNull Class<T> clazz) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(consumer), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedPutItemResponse<T> putItem(@NonNull Object keyObject,
			@NonNull Consumer<PutItemRequest.Builder> consumer) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.putRequestFromObject(keyObject);
		consumer.accept(builder);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull PutItemRequest request,
			@NonNull Class<T> clazz) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(request), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedPutItemResponse<T> putItem(@NonNull Object keyObject) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.putRequestFromObject(keyObject);
		return putItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(consumer), clazz);
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull Object hashKey,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<T> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return deleteItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull Object keyObject,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull DeleteItemRequest request,
			@NonNull Class<T> clazz) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(request), clazz);
	}

	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull Object hashKey, @NonNull Class<T> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		return deleteItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedDeleteItemResponse<T> deleteItem(@NonNull Object keyObject) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		return deleteItem(builder.build(), (Class<T>) keyObject.getClass());
	}
}
