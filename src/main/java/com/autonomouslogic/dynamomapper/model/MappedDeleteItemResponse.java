package com.autonomouslogic.dynamomapper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

@Value
public class MappedDeleteItemResponse<T> {
	DeleteItemResponse response;
	T item;
}
