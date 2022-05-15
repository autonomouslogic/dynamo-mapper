package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;

@Setter
public class DynamoAsyncMapperBuilder {
	private DynamoDbAsyncClient client;
	private ObjectMapper objectMapper;

	public DynamoAsyncMapper build() {
		var client = Optional.ofNullable(this.client)
			.orElseGet(DynamoDbAsyncClient::create);
		var objectMapper = Optional.ofNullable(this.objectMapper)
			.orElseGet(StdObjectMapper::objectMapper);
		return new DynamoAsyncMapper(client, objectMapper);
	}
}