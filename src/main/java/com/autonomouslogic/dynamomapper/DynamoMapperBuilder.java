package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;

@Setter
public class DynamoMapperBuilder {
	private DynamoDbClient client;
	private ObjectMapper objectMapper;

	public DynamoMapper build() {
		var client = Optional.ofNullable(this.client)
			.orElseGet(DynamoDbClient::create);
		var objectMapper = Optional.ofNullable(this.objectMapper)
			.orElseGet(StdObjectMapper::objectMapper);
		return new DynamoMapper(client, objectMapper);
	}
}
