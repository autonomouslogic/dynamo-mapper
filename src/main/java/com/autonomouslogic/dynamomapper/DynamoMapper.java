package com.autonomouslogic.dynamomapper;

import com.autonomouslogic.dynamomapper.codec.DynamoDecoder;
import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * See {@link DynamoEncoder} and {@link DynamoDecoder} for documentation.
 */
public class DynamoMapper {
	@Delegate
	private final DynamoEncoder encoder;
	@Delegate
	private final DynamoDecoder decoder;

	public DynamoMapper(@NonNull ObjectMapper objectMapper) {
		encoder = new DynamoEncoder(objectMapper);
		decoder = new DynamoDecoder(objectMapper);
	}
}
