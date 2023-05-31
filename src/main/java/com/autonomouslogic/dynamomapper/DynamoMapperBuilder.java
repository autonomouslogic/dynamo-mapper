package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Setter
public class DynamoMapperBuilder {
	private DynamoDbClient client;
	private ObjectMapper objectMapper;
	private TableNameDecorator tableNameDecorator;

	public DynamoMapper build() {
		var client = Optional.ofNullable(this.client).orElseGet(DynamoDbClient::create);
		var objectMapper = Optional.ofNullable(this.objectMapper).orElseGet(StdObjectMapper::objectMapper);
		return new DynamoMapper(client, objectMapper, tableNameDecorator);
	}
}
