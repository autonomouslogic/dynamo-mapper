package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Setter
public class DynamoAsyncMapperBuilder {
	private DynamoDbAsyncClient client;
	private ObjectMapper objectMapper;
	private TableNameDecorator tableNameDecorator;

	public DynamoAsyncMapper build() {
		var client = Optional.ofNullable(this.client).orElseGet(DynamoDbAsyncClient::create);
		var objectMapper = Optional.ofNullable(this.objectMapper).orElseGet(StdObjectMapper::objectMapper);
		return new DynamoAsyncMapper(client, objectMapper, tableNameDecorator);
	}
}
