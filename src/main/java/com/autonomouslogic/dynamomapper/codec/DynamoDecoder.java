package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Map;

@RequiredArgsConstructor
public class DynamoDecoder {
	@NonNull
	private final ObjectMapper objectMapper;

	public <T> MappedGetItemResponse<T> mapGetItemResponse(GetItemResponse response, Class<T> clazz) throws JsonProcessingException {
		var item = response.hasItem() ? decode(response.item(), clazz) : null;
		return new MappedGetItemResponse<>(response, item);
	}

	public <T> MappedPutItemResponse<T> mapPutItemResponse(PutItemResponse response, Class<T> clazz) throws JsonProcessingException {
		var item = response.hasAttributes() ? decode(response.attributes(), clazz) : null;
		return new MappedPutItemResponse<>(response, item);
	}

	public <T> MappedDeleteItemResponse<T> mapDeleteItemResponse(DeleteItemResponse response, Class<T> clazz) throws JsonProcessingException {
		var item = response.hasAttributes() ? decode(response.attributes(), clazz) : null;
		return new MappedDeleteItemResponse<>(response, item);
	}

	/**
	 * Decodes DynamoDB values into a POJO.
	 */
	public <T> T decode(Map<String, AttributeValue> map, Class<T> clazz) throws JsonProcessingException {
		ObjectNode json = objectMapper.createObjectNode();
		for (Map.Entry<String, AttributeValue> entry : map.entrySet()) {
			json.set(entry.getKey(), decodeValue(entry.getValue()));
		}
		return objectMapper.treeToValue(json, clazz);
	}

	private JsonNode decodeValue(AttributeValue val) {
		var nodeFactory = objectMapper.getNodeFactory();
		if (val.b() != null) {
			return decodeBinary(val.b());
		}
		if (val.bool() != null) {
			return nodeFactory.booleanNode(val.bool());
		}
		if (val.hasBs() && val.bs() != null) {
			return decodeBinarySet(val);
		}
		if (val.hasL() && val.l() != null) {
			return decodeList(val);
		}
		if (val.hasM() && val.m() != null) {
			return decodeMap(val);
		}
		if (val.n() != null) {
			return decodeNumber(val.n());
		}
		if (val.hasNs() && val.ns() != null) {
			return decodeNumberSet(val);
		}
		if (val.nul() != null) {
			return nodeFactory.nullNode();
		}
		if (val.s() != null) {
			return nodeFactory.textNode(val.s());
		}
		if (val.hasSs() && val.ss() != null) {
			return decodeStringSet(val);
		}
		throw new IllegalArgumentException(val.toString());
	}

	private JsonNode decodeBinary(SdkBytes bytes) {
		return objectMapper.getNodeFactory().binaryNode(bytes.asByteArray());
	}

	private JsonNode decodeNumber(String num) {
		return objectMapper.getNodeFactory().textNode(num);
	}

	private JsonNode decodeMap(AttributeValue val) {
		var obj = objectMapper.getNodeFactory().objectNode();
		for (var entry : val.m().entrySet()) {
			obj.set(entry.getKey(), decodeValue(entry.getValue()));
		}
		return obj;
	}

	private JsonNode decodeList(AttributeValue val) {
		var arr = objectMapper.getNodeFactory().arrayNode();
		for (var entry : val.l()) {
			arr.add(decodeValue(entry));
		}
		return arr;
	}

	private JsonNode decodeBinarySet(AttributeValue val) {
		var arr = objectMapper.getNodeFactory().arrayNode();
		for (var entry : val.bs()) {
			arr.add(decodeBinary(entry));
		}
		return arr;
	}

	private JsonNode decodeNumberSet(AttributeValue val) {
		var arr = objectMapper.getNodeFactory().arrayNode();
		for (var entry : val.ns()) {
			arr.add(decodeNumber(entry));
		}
		return arr;
	}

	private JsonNode decodeStringSet(AttributeValue val) {
		var nodeFactory = objectMapper.getNodeFactory();
		var arr = nodeFactory.arrayNode();
		for (var entry : val.ss()) {
			arr.add(nodeFactory.textNode(entry));
		}
		return arr;
	}
}
