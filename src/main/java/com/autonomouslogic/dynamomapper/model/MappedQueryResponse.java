package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;

@Value
public class MappedQueryResponse<T> {
	QueryResponse response;
	List<T> items;
}
