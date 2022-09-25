package com.autonomouslogic.dynamomapper.model;

import java.util.List;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@Value
public class MappedQueryResponse<T> {
	QueryResponse response;
	List<T> items;
}
