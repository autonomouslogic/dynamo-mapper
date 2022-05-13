package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ItemCollectionSizeLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.RequestLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.TransactionConflictException;

import java.io.IOException;
import java.util.function.Consumer;

/**
 */
public class DynamoMapper {
	private final DynamoDbClient client;
	private final DynamoEncoder encoder;
	private final DynamoDecoder decoder;
	private final RequestFactory requestFactory;
	private final ReflectionUtil reflectionUtil;

	public DynamoMapper(@NonNull DynamoDbClient client, @NonNull ObjectMapper objectMapper) {
		this.client = client;
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
		reflectionUtil = new ReflectionUtil(objectMapper);
		requestFactory = new RequestFactory(encoder, objectMapper, reflectionUtil);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Object hashKey, @NonNull Class<T> clazz) throws IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		return getItem(builder.build(), clazz);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Object hashKey, @NonNull Consumer<GetItemRequest.Builder> getItemRequest, @NonNull Class<T> clazz) throws IOException {
		var builder = requestFactory.getRequestFromHashKey(hashKey, clazz);
		getItemRequest.accept(builder);
		return getItem(builder.build(), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItem(@NonNull T keyObject) throws IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> MappedGetItemResponse<T> getItem(@NonNull T keyObject, @NonNull Consumer<GetItemRequest.Builder> getItemRequest) throws IOException {
		var builder = requestFactory.getRequestFromKeyObject(keyObject);
		getItemRequest.accept(builder);
		return getItem(builder.build(), (Class<T>) keyObject.getClass());
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull GetItemRequest request, @NonNull Class<T> clazz) throws JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(request), clazz);
	}

	public <T> MappedGetItemResponse<T> getItem(@NonNull Consumer<GetItemRequest.Builder> getItemRequest, @NonNull Class<T> clazz) throws AwsServiceException, SdkClientException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(getItemRequest), clazz);
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull Object obj) throws IOException {
		return putItem(requestFactory.putRequestFromObject(obj).build());
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull Object obj, @NonNull Consumer<PutItemRequest.Builder> putItemRequest) throws IOException {
		var builder = requestFactory.putRequestFromObject(obj);
		putItemRequest.accept(builder);
		return putItem(builder.build());
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull PutItemRequest putItemRequest, @NonNull Class<T> clazz) throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException, ItemCollectionSizeLimitExceededException, TransactionConflictException, RequestLimitExceededException, InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(putItemRequest), clazz);
	}

	public <T> MappedPutItemResponse<T> putItem(@NonNull Consumer<PutItemRequest.Builder> putItemRequest, @NonNull Class<T> clazz) throws ConditionalCheckFailedException, ProvisionedThroughputExceededException, ResourceNotFoundException, ItemCollectionSizeLimitExceededException, TransactionConflictException, RequestLimitExceededException, InternalServerErrorException, AwsServiceException, SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapPutItemResponse(client.putItem(putItemRequest), clazz);
	}
}
