package com.autonomouslogic.dynamomapper.codec;

import com.autonomouslogic.dynamomapper.model.MappedBatchGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedQueryResponse;
import com.autonomouslogic.dynamomapper.model.MappedScanResponse;
import com.autonomouslogic.dynamomapper.model.MappedUpdateItemResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.BooleanNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

@RequiredArgsConstructor
public class DynamoDecoder {
	@NonNull
	private final JsonMapper jsonMapper;

	public <T> MappedGetItemResponse<T> mapGetItemResponse(GetItemResponse response, Class<T> clazz)
			throws JacksonException {
		var item = response.hasItem() ? decode(response.item(), clazz) : null;
		return new MappedGetItemResponse<>(response, item);
	}

	public <T> MappedBatchGetItemResponse<T> mapBatchGetItemResponse(BatchGetItemResponse response, Class<T> clazz)
			throws JacksonException {
		Map<String, List<T>> decodedResponses = null;
		if (response.hasResponses()) {
			decodedResponses = new LinkedHashMap<>();
			var responses = response.responses();
			for (var entry : responses.entrySet()) {
				var table = entry.getKey();
				var values = entry.getValue();
				List<T> items = new ArrayList<>(values.size());
				decodedResponses.put(table, items);
				for (var value : values) {
					items.add(decode(value, clazz));
				}
			}
		}
		return new MappedBatchGetItemResponse<>(response, decodedResponses);
	}

	public <T> MappedPutItemResponse<T> mapPutItemResponse(PutItemResponse response, Class<T> clazz)
			throws JacksonException {
		var item = response.hasAttributes() ? decode(response.attributes(), clazz) : null;
		return new MappedPutItemResponse<>(response, item);
	}

	public <T> MappedUpdateItemResponse<T> mapUpdateItemResponse(UpdateItemResponse response, Class<T> clazz)
			throws JacksonException {
		var item = response.hasAttributes() ? decode(response.attributes(), clazz) : null;
		return new MappedUpdateItemResponse<>(response, item);
	}

	public <T> MappedDeleteItemResponse<T> mapDeleteItemResponse(DeleteItemResponse response, Class<T> clazz)
			throws JacksonException {
		var item = response.hasAttributes() ? decode(response.attributes(), clazz) : null;
		return new MappedDeleteItemResponse<>(response, item);
	}

	public <T> MappedScanResponse<T> mapScanResponse(ScanResponse response, Class<T> clazz) throws JacksonException {
		var decoded = decodeItems(response.items(), clazz);
		return new MappedScanResponse<>(response, decoded);
	}

	public <T> MappedQueryResponse<T> mapQueryResponse(QueryResponse response, Class<T> clazz) throws JacksonException {
		var decoded = decodeItems(response.items(), clazz);
		return new MappedQueryResponse<>(response, decoded);
	}

	private <T> List<T> decodeItems(List<Map<String, AttributeValue>> items, Class<T> clazz) throws JacksonException {
		if (items == null) {
			return null;
		}
		var decoded = new ArrayList<T>(items.size());
		if (!items.isEmpty()) {
			decoded = new ArrayList<>(items.size());
			for (var item : items) {
				decoded.add(decode(item, clazz));
			}
		}
		return decoded;
	}

	/**
	 * Decodes DynamoDB values into a POJO.
	 */
	public <T> T decode(Map<String, AttributeValue> map, Class<T> clazz) throws JacksonException {
		ObjectNode json = jsonMapper.createObjectNode();
		for (Map.Entry<String, AttributeValue> entry : map.entrySet()) {
			json.set(entry.getKey(), decodeValue(entry.getValue()));
		}
		return jsonMapper.treeToValue(json, clazz);
	}

	private JsonNode decodeValue(AttributeValue val) {
		var nodeFactory = jsonMapper.getNodeFactory();
		if (val.b() != null) {
			return decodeBinary(val.b());
		}
		if (val.bool() != null) {
			return decodeBoolean(val);
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
			return decodeString(val.s());
		}
		if (val.hasSs() && val.ss() != null) {
			return decodeStringSet(val);
		}
		throw new IllegalArgumentException(val.toString());
	}

	private BooleanNode decodeBoolean(AttributeValue val) {
		return jsonMapper.getNodeFactory().booleanNode(val.bool());
	}

	private StringNode decodeString(String val) {
		return jsonMapper.getNodeFactory().stringNode(val);
	}

	private JsonNode decodeBinary(SdkBytes bytes) {
		return jsonMapper.getNodeFactory().binaryNode(bytes.asByteArray());
	}

	private JsonNode decodeNumber(String num) {
		try {
			var val = Long.parseLong(num);
			return jsonMapper.getNodeFactory().numberNode(val);
		} catch (NumberFormatException e) {
			return decodeString(num);
		}
	}

	private JsonNode decodeMap(AttributeValue val) {
		var obj = jsonMapper.getNodeFactory().objectNode();
		for (var entry : val.m().entrySet()) {
			obj.set(entry.getKey(), decodeValue(entry.getValue()));
		}
		return obj;
	}

	private JsonNode decodeList(AttributeValue val) {
		var arr = jsonMapper.getNodeFactory().arrayNode();
		for (var entry : val.l()) {
			arr.add(decodeValue(entry));
		}
		return arr;
	}

	private JsonNode decodeBinarySet(AttributeValue val) {
		var arr = jsonMapper.getNodeFactory().arrayNode();
		for (var entry : val.bs()) {
			arr.add(decodeBinary(entry));
		}
		return arr;
	}

	private JsonNode decodeNumberSet(AttributeValue val) {
		return decodeArray(val.ns(), this::decodeNumber);
	}

	private JsonNode decodeStringSet(AttributeValue val) {
		return decodeArray(val.ss(), this::decodeString);
	}

	private <T> ArrayNode decodeArray(List<T> vals, Function<T, JsonNode> f) {
		var arr = jsonMapper.getNodeFactory().arrayNode();
		for (var val : vals) {
			arr.add(f.apply(val));
		}
		return arr;
	}
}
