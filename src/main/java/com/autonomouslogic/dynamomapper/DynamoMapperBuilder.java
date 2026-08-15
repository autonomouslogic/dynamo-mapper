package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.autonomouslogic.dynamomapper.util.StdObjectMapper;
import java.util.Optional;
import lombok.Setter;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import tools.jackson.databind.json.JsonMapper;

@Setter
public class DynamoMapperBuilder {
	private DynamoDbClient client;
	private JsonMapper jsonMapper;
	private TableNameDecorator tableNameDecorator;

	public DynamoMapper build() {
		var client = Optional.ofNullable(this.client).orElseGet(DynamoDbClient::create);
		var jsonMapper = Optional.ofNullable(this.jsonMapper).orElseGet(StdObjectMapper::jsonMapper);
		return new DynamoMapper(client, jsonMapper, tableNameDecorator);
	}
}
