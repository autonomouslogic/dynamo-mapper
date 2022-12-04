package com.autonomouslogic.dynamomapper.codegen.generate;

import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.CLASS_T;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.field;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedBatchGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedDeleteItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedPutItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedQueryResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedScanResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedUpdateItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.overridableMethods;

import com.autonomouslogic.dynamomapper.codegen.generate.keyobject.KeyObjectWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.primarykey.PrimaryKeyWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.MethodNameFactory;
import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import lombok.RequiredArgsConstructor;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

@RequiredArgsConstructor
public class SyncMapperGenerator {
	public static final String REQUEST = "request";
	public static final String CONSUMER = "consumer";

	protected final TypeSpec.Builder mapper;
	protected final Logger log;
	protected final MethodNameFactory methodNameFactory = new MethodNameFactory();
	protected final Supplier<PrimaryKeyWrapperGenerator> primaryKeyWrapperGeneratorSupplier;
	protected final Supplier<KeyObjectWrapperGenerator> keyObjectWrapperGeneratorSupplier;

	protected FieldSpec clientField;
	protected FieldSpec encoderField;
	protected FieldSpec decoderField;
	protected FieldSpec requestFactoryField;
	protected FieldSpec reflectionUtilField;

	public void generate() {
		generateFields();
		generateConstructor();
		generateGetWrappers();
		generateBatchGetWrappers();
		generatePutWrappers();
		generateUpdateWrappers();
		generateDeleteWrappers();
		generateScanWrappers();
		generateQueryWrappers();
		generateBuilder();
	}

	protected Class<?> clientClass() {
		return DynamoDbClient.class;
	}

	protected ClassName builderClass() {
		return TypeHelper.dynamoMapperBuilder;
	}

	protected void generateFields() {
		clientField = field(clientClass(), "client");
		mapper.addField(clientField);
		encoderField = field(TypeHelper.dynamoEncoder, "encoder");
		mapper.addField(encoderField);
		decoderField = field(TypeHelper.dynamoDecoder, "decoder");
		mapper.addField(decoderField);
		requestFactoryField = field(TypeHelper.requestFactory, "requestFactory");
		mapper.addField(requestFactoryField);
		reflectionUtilField = field(TypeHelper.reflectionUtil, "reflectionUtil");
		mapper.addField(reflectionUtilField);
	}

	protected void generateConstructor() {
		var constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PROTECTED);
		var client = ParameterSpec.builder(clientField.type, "client").build();
		var objectMapper =
				ParameterSpec.builder(ObjectMapper.class, "objectMapper").build();
		constructor.addParameter(client);
		constructor.addParameter(objectMapper);

		constructor
				.addStatement("this.client = client")
				.addStatement("encoder = new $T(objectMapper)", encoderField.type)
				.addStatement("decoder = new $T(objectMapper)", decoderField.type)
				.addStatement("reflectionUtil = new $T(objectMapper)", reflectionUtilField.type)
				.addStatement(
						"requestFactory = new $T(encoder, objectMapper, reflectionUtil)", requestFactoryField.type);

		mapper.addMethod(constructor.build());
	}

	protected void generateGetWrappers() {
		for (Method method : overridableMethods(clientClass(), "getItem")) {
			var delegate = generateDelegateWrapper(
					method, mappedGetItemResponse, "mapGetItemResponse", GetItemRequest.class, GetItemResponse.class);
			mapper.addMethod(primaryKeyWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("getItemRequestFromPrimaryKey")
					.generate());
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("getItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateBatchGetWrappers() {
		for (Method method : overridableMethods(clientClass(), "batchGetItem")) {
			var delegate = generateDelegateWrapper(
					method,
					mappedBatchGetItemResponse,
					"mapBatchGetItemResponse",
					BatchGetItemRequest.class,
					BatchGetItemResponse.class);

			mapper.addMethod(primaryKeyWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("batchGetItemRequestFromPrimaryKeys")
					.multiple(true)
					.futureWrap(true)
					.generate());
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("batchGetItemRequestFromKeyObjects")
					.multiple(true)
					.generate());
		}
	}

	protected void generatePutWrappers() {
		for (Method method : overridableMethods(clientClass(), "putItem")) {
			var delegate = generateDelegateWrapper(
					method, mappedPutItemResponse, "mapPutItemResponse", PutItemRequest.class, PutItemResponse.class);
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("putItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateUpdateWrappers() {
		for (Method method : overridableMethods(clientClass(), "updateItem")) {
			var delegate = generateDelegateWrapper(
					method,
					mappedUpdateItemResponse,
					"mapUpdateItemResponse",
					UpdateItemRequest.class,
					UpdateItemResponse.class);
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("updateItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateDeleteWrappers() {
		for (Method method : overridableMethods(clientClass(), "deleteItem")) {
			var delegate = generateDelegateWrapper(
					method,
					mappedDeleteItemResponse,
					"mapDeleteItemResponse",
					DeleteItemRequest.class,
					DeleteItemResponse.class);
			mapper.addMethod(primaryKeyWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("deleteItemRequestFromPrimaryKey")
					.futureWrap(true)
					.generate());
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("deleteItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateScanWrappers() {
		for (Method method : overridableMethods(clientClass(), "scan")) {
			var delegate = generateDelegateWrapper(
					method, mappedScanResponse, "mapScanResponse", ScanRequest.class, ScanResponse.class);
		}
	}

	protected void generateQueryWrappers() {
		for (Method method : overridableMethods(clientClass(), "query")) {
			var delegate = generateDelegateWrapper(
					method, mappedQueryResponse, "mapQueryResponse", QueryRequest.class, QueryResponse.class);
		}
	}

	protected MethodSpec generateDelegateWrapper(
			Method method, ClassName returnType, String decoderMethod, Class<?> requestClass, Class<?> responseClass) {
		var requestVar = detectRequestOrConsumer(method);
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.getName())
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(TypeHelper.genericCapture(returnType));
		wrapper.addExceptions(
				Stream.of(method.getExceptionTypes()).map(e -> ClassName.get(e)).collect(Collectors.toList()));
		wrapper.addException(JsonProcessingException.class);
		// Add parameters.
		var delegateParams = List.of(method.getGenericParameterTypes());
		if (delegateParams.size() != 1) {
			throw new IllegalArgumentException(String.format(
					"Delegate param generation only supports 1 param, %s seen for %s",
					delegateParams.size(), method.getName()));
		}
		wrapper.addParameter(delegateParams.get(0), requestVar);
		wrapper.addParameter(CLASS_T, "clazz");
		// Write body.
		if (requestVar.equals(REQUEST)) {
			generateRequestObjectWrapper(wrapper, requestClass, requestVar);
		} else if (requestVar.equals(CONSUMER)) {
			generateRequestConsumerWrapper(wrapper, requestClass, requestVar);
		} else {
			throw new RuntimeException();
		}
		wrapper.addStatement("return decoder.$L(client.$L(reqOrConsumer), clazz)", decoderMethod, method.getName());

		TypeHelper.nonNullParameters(wrapper);
		var built = wrapper.build();
		mapper.addMethod(built);
		return built;
	}

	protected void generateRequestObjectWrapper(MethodSpec.Builder wrapper, Class<?> requestClass, String requestVar) {
		wrapper.addStatement(
				"var reqOrConsumer = requestFactory.accept$L($L, clazz)", requestClass.getSimpleName(), requestVar);
	}

	protected void generateRequestConsumerWrapper(
			MethodSpec.Builder wrapper, Class<?> requestClass, String requestVar) {
		wrapper.beginControlFlow("Consumer<$T.Builder> reqOrConsumer = (builder) -> {", requestClass)
				.addStatement("requestFactory.accept$L(builder, clazz)", requestClass.getSimpleName())
				.addStatement("$L.accept(builder)", requestVar)
				.endControlFlow("}");
	}

	protected void generateBuilder() {
		mapper.addMethod(MethodSpec.methodBuilder("builder")
				.addModifiers(Modifier.PUBLIC)
				.addModifiers(Modifier.STATIC)
				.returns(builderClass())
				.addStatement("return new $T()", builderClass())
				.build());
	}

	protected String detectRequestOrConsumer(Method method) {
		var requestVar = REQUEST;
		var firstParamTypeName = method.getParameterTypes()[0];
		if (firstParamTypeName.equals(Consumer.class)) {
			requestVar = CONSUMER;
		}
		return requestVar;
	}
}
