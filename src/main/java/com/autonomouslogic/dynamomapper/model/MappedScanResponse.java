package com.autonomouslogic.dynamomapper.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;

@Value
public class MappedScanResponse<T> {
	ScanResponse response;
	List<T> items;
}
