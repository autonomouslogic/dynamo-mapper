package com.autonomouslogic.dynamomapper.model;


import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

@Value
public class MappedGetItemResponse<T> {
	GetItemResponse response;
	T item;
}
