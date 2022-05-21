package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@Value
public class MappedUpdateItemResponse<T> {
	UpdateItemResponse response;
	T item;
}
