package com.autonomouslogic.dynamomapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.autonomouslogic.dynamomapper.codec.DynamoEncoder;
import com.autonomouslogic.dynamomapper.model.IntegrationTestObject;
import com.autonomouslogic.dynamomapper.model.MappedDeleteItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedGetItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedPutItemResponse;
import com.autonomouslogic.dynamomapper.model.MappedUpdateItemResponse;
import com.autonomouslogic.dynamomapper.test.FailBiFunction;
import com.autonomouslogic.dynamomapper.test.FailFunction;
import com.autonomouslogic.dynamomapper.test.FailTriFunction;
import com.autonomouslogic.dynamomapper.test.IntegrationTestUtil;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.Value;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Setter
@RequiredArgsConstructor
public class PermutationTester {
	public enum MethodType {
		REQUEST,
		PRIMARY_KEY,
		KEY_OBJECT
	}

	public enum CallMethod {
		STRAIGHT,
		CONSUMER
	}

	@Value
	public static final class ObjectAndTestMethod {
		IntegrationTestObject obj;
		MethodType methodType;
		CallMethod callMethod;
	}

	private final DynamoEncoder encoder;

	private IntegrationTestObject obj;
	private MethodType methodType;
	private CallMethod callMethod;

	// Get methods.
	private FailBiFunction<GetItemRequest, Class<IntegrationTestObject>, MappedGetItemResponse<IntegrationTestObject>>
			getItemRequestStraight;
	private FailBiFunction<
					Consumer<GetItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedGetItemResponse<IntegrationTestObject>>
			getItemRequestConsumer;
	private FailBiFunction<String, Class<IntegrationTestObject>, MappedGetItemResponse<IntegrationTestObject>>
			getItemPrimaryKeyStraight;
	private FailTriFunction<
					String,
					Consumer<GetItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedGetItemResponse<IntegrationTestObject>>
			getItemPrimaryKeyConsumer;
	private FailFunction<IntegrationTestObject, MappedGetItemResponse<IntegrationTestObject>> getItemKeyObjectStraight;
	private FailBiFunction<
					IntegrationTestObject,
					Consumer<GetItemRequest.Builder>,
					MappedGetItemResponse<IntegrationTestObject>>
			getItemKeyObjectConsumer;

	// Put methods.
	private FailBiFunction<PutItemRequest, Class<IntegrationTestObject>, MappedPutItemResponse<IntegrationTestObject>>
			putItemRequestStraight;
	private FailBiFunction<
					Consumer<PutItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedPutItemResponse<IntegrationTestObject>>
			putItemRequestConsumer;
	private FailFunction<IntegrationTestObject, MappedPutItemResponse<IntegrationTestObject>> putItemKeyObjectStraight;
	private FailBiFunction<
					IntegrationTestObject,
					Consumer<PutItemRequest.Builder>,
					MappedPutItemResponse<IntegrationTestObject>>
			putItemKeyObjectConsumer;

	// Update methods.
	private FailBiFunction<
					UpdateItemRequest, Class<IntegrationTestObject>, MappedUpdateItemResponse<IntegrationTestObject>>
			updateItemRequestStraight;
	private FailBiFunction<
					Consumer<UpdateItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedUpdateItemResponse<IntegrationTestObject>>
			updateItemRequestConsumer;
	private FailFunction<IntegrationTestObject, MappedUpdateItemResponse<IntegrationTestObject>>
			updateItemKeyObjectStraight;
	private FailBiFunction<
					IntegrationTestObject,
					Consumer<UpdateItemRequest.Builder>,
					MappedUpdateItemResponse<IntegrationTestObject>>
			updateItemKeyObjectConsumer;

	// Delete methods.
	private FailBiFunction<
					DeleteItemRequest, Class<IntegrationTestObject>, MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemRequestStraight;
	private FailBiFunction<
					Consumer<DeleteItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemRequestConsumer;
	private FailBiFunction<String, Class<IntegrationTestObject>, MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemPrimaryKeyStraight;
	private FailTriFunction<
					String,
					Consumer<DeleteItemRequest.Builder>,
					Class<IntegrationTestObject>,
					MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemPrimaryKeyConsumer;
	private FailFunction<IntegrationTestObject, MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemKeyObjectStraight;
	private FailBiFunction<
					IntegrationTestObject,
					Consumer<DeleteItemRequest.Builder>,
					MappedDeleteItemResponse<IntegrationTestObject>>
			deleteItemKeyObjectConsumer;

	public void runTest() {
		// Get
		var emptyGetResponse = testGet(obj);
		assertNull(emptyGetResponse.item());
		// Put.
		testPut(obj);
		// Get.
		var getResponse = testGet(obj);
		assertEquals(obj, getResponse.item());
		// Update.
		var obj2 = obj.toBuilder().str("new-val").build();
		var updateResponse = testUpdate(obj2);
		if (methodType == MethodType.REQUEST || callMethod == CallMethod.CONSUMER) {
			assertEquals(obj2, updateResponse.item());
		} else {
			assertNull(updateResponse.item());
		}
		// Delete.
		var deleteResponse = testDelete(obj2);
		if (methodType == MethodType.REQUEST || callMethod == CallMethod.CONSUMER) {
			assertEquals(obj2, deleteResponse.item());
		} else {
			assertNull(updateResponse.item());
		}
		// Get
		var emptyGetResponse2 = testGet(obj);
		assertNull(emptyGetResponse2.item());
	}

	@SneakyThrows
	private MappedGetItemResponse<IntegrationTestObject> testGet(IntegrationTestObject obj) {
		var key = encoder.encodeKeyValue(obj);
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return getItemRequestStraight.apply(
								GetItemRequest.builder().key(key).build(), IntegrationTestObject.class);
					case CONSUMER:
						return getItemRequestConsumer.apply(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.key(key);
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY:
				switch (callMethod) {
					case STRAIGHT:
						return getItemPrimaryKeyStraight.apply(obj.partitionKey(), IntegrationTestObject.class);
					case CONSUMER:
						return getItemPrimaryKeyConsumer.apply(
								obj.partitionKey(),
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									assertEquals(
											obj.partitionKey(),
											req.key().get("partitionKey").s());
								},
								IntegrationTestObject.class);
				}
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return getItemKeyObjectStraight.apply(obj);
					case CONSUMER:
						return getItemKeyObjectConsumer.apply(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(
									obj.partitionKey(),
									req.key().get("partitionKey").s());
						});
				}
		}
		throw new IllegalStateException();
	}

	@SneakyThrows
	private MappedPutItemResponse<IntegrationTestObject> testPut(IntegrationTestObject obj) {
		var item = encoder.encode(obj);
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return putItemRequestStraight.apply(
								PutItemRequest.builder().item(item).build(), IntegrationTestObject.class);
					case CONSUMER:
						return putItemRequestConsumer.apply(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.item(item);
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY: // Puts don't have a straight primary-key method, just use key objects.
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return putItemKeyObjectStraight.apply(obj);
					case CONSUMER:
						return putItemKeyObjectConsumer.apply(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(item, req.item());
						});
				}
		}
		throw new IllegalStateException();
	}

	@SneakyThrows
	private MappedUpdateItemResponse<IntegrationTestObject> testUpdate(IntegrationTestObject obj) {
		var key = encoder.encodeKeyValue(obj);
		var updates = encoder.encodeUpdates(obj);
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return updateItemRequestStraight.apply(
								UpdateItemRequest.builder()
										.key(key)
										.attributeUpdates(updates)
										.returnValues(ReturnValue.ALL_NEW)
										.build(),
								IntegrationTestObject.class);
					case CONSUMER:
						return updateItemRequestConsumer.apply(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.key(key).attributeUpdates(updates).returnValues(ReturnValue.ALL_NEW);
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY: // Updates don't have a straight primary-key method, just use key objects.
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return updateItemKeyObjectStraight.apply(obj);
					case CONSUMER:
						return updateItemKeyObjectConsumer.apply(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(key, req.key());
							assertEquals(updates, req.attributeUpdates());
							builder.returnValues(ReturnValue.ALL_NEW);
						});
				}
		}
		throw new IllegalStateException();
	}

	@SneakyThrows
	private MappedDeleteItemResponse<IntegrationTestObject> testDelete(IntegrationTestObject obj) {
		var key = encoder.encodeKeyValue(obj);
		switch (methodType) {
			case REQUEST:
				switch (callMethod) {
					case STRAIGHT:
						return deleteItemRequestStraight.apply(
								DeleteItemRequest.builder()
										.key(key)
										.returnValues(ReturnValue.ALL_OLD)
										.build(),
								IntegrationTestObject.class);
					case CONSUMER:
						return deleteItemRequestConsumer.apply(
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									builder.key(key).returnValues(ReturnValue.ALL_OLD);
								},
								IntegrationTestObject.class);
				}
			case PRIMARY_KEY:
				switch (callMethod) {
					case STRAIGHT:
						return deleteItemPrimaryKeyStraight.apply(obj.partitionKey(), IntegrationTestObject.class);
					case CONSUMER:
						return deleteItemPrimaryKeyConsumer.apply(
								obj.partitionKey(),
								builder -> {
									var req = builder.build();
									assertEquals("integration-test-table", req.tableName());
									assertEquals(key, req.key());
									builder.returnValues(ReturnValue.ALL_OLD);
								},
								IntegrationTestObject.class);
				}
			case KEY_OBJECT:
				switch (callMethod) {
					case STRAIGHT:
						return deleteItemKeyObjectStraight.apply(obj);
					case CONSUMER:
						return deleteItemKeyObjectConsumer.apply(obj, builder -> {
							var req = builder.build();
							assertEquals("integration-test-table", req.tableName());
							assertEquals(key, req.key());
							builder.returnValues(ReturnValue.ALL_OLD);
						});
				}
		}
		throw new IllegalStateException();
	}

	public static List<ObjectAndTestMethod> objectAndTestMethods() {
		return IntegrationTestUtil.loadIntegrationTestObjects().stream()
				.flatMap(obj -> Stream.of(MethodType.values()).flatMap(methodType -> Stream.of(CallMethod.values())
						.map(callMethod -> new ObjectAndTestMethod(obj, methodType, callMethod))))
				.collect(Collectors.toList());
	}
}
