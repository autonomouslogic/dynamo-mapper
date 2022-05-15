/*
 This is a generated file, do not edit manually.
*/
package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.request.RequestFactory;
import com.autonomouslogic.dynamomapper.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

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
}
