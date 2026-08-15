package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.util.StdJsonMapper;
import java.util.Optional;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import tools.jackson.databind.json.JsonMapper;

@Setter
public class DynamoAsyncMapperBuilder {
	private DynamoDbAsyncClient client;
	private JsonMapper jsonMapper;
	private TableNameDecorator tableNameDecorator;

	public DynamoAsyncMapper build() {
		var client = Optional.ofNullable(this.client).orElseGet(DynamoDbAsyncClient::create);
		var jsonMapper = Optional.ofNullable(this.jsonMapper).orElseGet(StdJsonMapper::jsonMapper);
		return new DynamoAsyncMapper(client, jsonMapper, tableNameDecorator);
	}
}
