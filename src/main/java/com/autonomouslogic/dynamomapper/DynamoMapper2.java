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

	public DynamoMapper2(@NonNull DynamoDbClient client, @NonNull ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public MappedGetItemResponse<?> getItem(GetItemRequest request, Class<?> clazz) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(request), clazz);
	}

	public MappedGetItemResponse<?> getItem(Object hashKey, Class<?> clazz) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		return getItem(builder.build(), clazz);
	}

	public MappedGetItemResponse<?> getItem(Object keyObject) throws
			ProvisionedThroughputExceededException, ResourceNotFoundException, RequestLimitExceededException,
			InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException,
			JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		return getItem(builder.build(), keyObject.getClass());
	}

	public MappedGetItemResponse<?> getItem(Consumer<GetItemRequest.Builder> consumer, Class<?> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(consumer), clazz);
	}

	public MappedGetItemResponse<?> getItem(Object hashKey, Consumer<GetItemRequest.Builder> consumer,
			Class<?> clazz) throws ProvisionedThroughputExceededException, ResourceNotFoundException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return getItem(builder.build(), clazz);
	}

	public MappedGetItemResponse<?> getItem(Object keyObject,
			Consumer<GetItemRequest.Builder> consumer) throws ProvisionedThroughputExceededException,
			ResourceNotFoundException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return getItem(builder.build(), keyObject.getClass());
	}

	public MappedPutItemResponse<?> putItem(Consumer<PutItemRequest.Builder> consumer, Class<?> clazz)
			throws ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(consumer), clazz);
	}

	public MappedPutItemResponse<?> putItem(PutItemRequest request, Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(request), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(Consumer<DeleteItemRequest.Builder> consumer,
			Class<?> clazz) throws ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(consumer), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(Object hashKey,
			Consumer<DeleteItemRequest.Builder> consumer, Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		consumer.accept(builder);
		return deleteItem(builder.build(), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(Object keyObject,
			Consumer<DeleteItemRequest.Builder> consumer) throws ConditionalCheckFailedException,
			ProvisionedThroughputExceededException, ResourceNotFoundException,
			ItemCollectionSizeLimitExceededException, TransactionConflictException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException, IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		consumer.accept(builder);
		return deleteItem(builder.build(), keyObject.getClass());
	}

	public MappedDeleteItemResponse<?> deleteItem(DeleteItemRequest request, Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapDeleteItemResponse(client.deleteItem(request), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(Object hashKey, Class<?> clazz) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromHashKey(hashKey, clazz);
		return deleteItem(builder.build(), clazz);
	}

	public MappedDeleteItemResponse<?> deleteItem(Object keyObject) throws
			ConditionalCheckFailedException, ProvisionedThroughputExceededException,
			ResourceNotFoundException, ItemCollectionSizeLimitExceededException,
			TransactionConflictException, RequestLimitExceededException, InternalServerErrorException,
			AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException,
			IOException {
		var builder = requestFactory.deleteRequestFromKeyObject(keyObject);
		return deleteItem(builder.build(), keyObject.getClass());
	}
}
