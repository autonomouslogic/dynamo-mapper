package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.List;

@Value
public class MappedQueryResponse<T> {
	QueryResponse response;
	List<T> items;
}
