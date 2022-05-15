/*
 This is a generated file, do not edit manually.
*/
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Class;
import java.util.function.Consumer;
import lombok.NonNull;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.RequestLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

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

	public MappedGetItemResponse<?> getItem(Consumer<GetItemRequest.Builder> request, Class<?> clazz)
			throws ProvisionedThroughputExceededException, ResourceNotFoundException,
			RequestLimitExceededException, InternalServerErrorException, AwsServiceException,
			SdkClientException, DynamoDbException, JsonProcessingException {
		return decoder.mapGetItemResponse(client.getItem(request), clazz);
	}
}
