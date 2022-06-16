package com.autonomouslogic.dynamomapper.model;

import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;

@Value
public class MappedScanResponse<T> {
	ScanResponse response;
	List<T> items;
}
