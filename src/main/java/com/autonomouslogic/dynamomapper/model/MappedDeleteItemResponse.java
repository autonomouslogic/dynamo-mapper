package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;

@Value
public class MappedDeleteItemResponse<T> {
	DeleteItemResponse response;
	T item;
}
