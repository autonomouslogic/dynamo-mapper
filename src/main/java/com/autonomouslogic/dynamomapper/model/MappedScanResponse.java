package com.autonomouslogic.dynamomapper.model;

import java.util.List;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@Value
public class MappedScanResponse<T> {
	ScanResponse response;
	List<T> items;
}
