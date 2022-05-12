package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

@Value
public class MappedPutItemResponse<T> {
	PutItemResponse response;
	T item;
}
