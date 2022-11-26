package com.autonomouslogic.dynamomapper.model;

import java.util.List;
import java.util.Map;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;

@Value
public class MappedBatchGetItemResponse<T> {
	BatchGetItemResponse response;
	Map<String, List<T>> items;
}
