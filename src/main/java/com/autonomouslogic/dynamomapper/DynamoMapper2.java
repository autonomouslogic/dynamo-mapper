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

class DynamoMapper2 {
	final DynamoDbClient client;

	final DynamoEncoder encoder;

	final DynamoDecoder decoder;

	final RequestFactory requestFactory;

	final ReflectionUtil reflectionUtil;

	public DynamoMapper2(DynamoDbClient client, ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public MappedGetItemResponse<?> getItem(@NonNull GetItemRequest request, @NonNull Class<?> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(request), clazz);
	}

	public MappedGetItemResponse<?> getItem(@NonNull Object hashKey, @NonNull Class<?> clazz) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		return getItem(builder.build(), clazz);
	}

	public MappedGetItemResponse<?> getItem(@NonNull Object keyObject) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		return getItem(builder.build(), keyObject.getClass());
	}

	public MappedGetItemResponse<?> getItem(@NonNull Consumer<GetItemRequest.Builder> consumer,
			@NonNull Class<?> clazz) throws ProvisionedThroughputExceededException,
			ResourceNotFoundException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(consumer), clazz);
	}

	public MappedGetItemResponse<?> getItem(@NonNull Object hashKey,
			@NonNull Consumer<GetItemRequest.Builder> consumer, @NonNull Class<?> clazz) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return getItem(builder.build(), clazz);
	}

	public MappedGetItemResponse<?> getItem(@NonNull Object keyObject,
			@NonNull Consumer<GetItemRequest.Builder> consumer) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return getItem(builder.build(), keyObject.getClass());
	}

	public MappedPutItemResponse<?> putItem(@NonNull Consumer<PutItemRequest.Builder> consumer,
			@NonNull Class<?> clazz) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(consumer), clazz);
	}

	public MappedPutItemResponse<?> putItem(@NonNull Object keyObject,
			@NonNull Consumer<PutItemRequest.Builder> consumer) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.putRequestFromObject(keyObject);
		consumer.accept(builder);
		return putItem(builder.build(), keyObject.getClass());
	}

	public MappedPutItemResponse<?> putItem(@NonNull PutItemRequest request, @NonNull Class<?> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(request), clazz);
	}

	public MappedPutItemResponse<?> putItem(@NonNull Object keyObject) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.putRequestFromObject(keyObject);
		return putItem(builder.build(), keyObject.getClass());
	}

	public MappedDeleteItemResponse<?> deleteItem(
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(consumer), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(@NonNull Object hashKey,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer, @NonNull Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return deleteItem(builder.build(), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(@NonNull Object keyObject,
			@NonNull Consumer<DeleteItemRequest.Builder> consumer) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return deleteItem(builder.build(), keyObject.getClass());
	}

	public MappedDeleteItemResponse<?> deleteItem(@NonNull DeleteItemRequest request,
			@NonNull Class<?> clazz) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(request), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(@NonNull Object hashKey, @NonNull Class<?> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		return deleteItem(builder.build(), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(@NonNull Object keyObject) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		return deleteItem(builder.build(), keyObject.getClass());
	}
}
