package com.autonomouslogic.dynamomapper.codegen.generate;

import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.field;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedBatchGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedDeleteItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedPutItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedQueryResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedScanResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedUpdateItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.overridableMethods;

import com.autonomouslogic.dynamomapper.codegen.generate.delegate.DelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.keyobject.KeyObjectWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.primarykey.PrimaryKeyWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.lang.reflect.Method;
import java.util.function.Supplier;
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
	protected final TypeSpec.Builder mapperTest;
	protected final Logger log;
	protected final Supplier<DelegateWrapperGenerator> delegateWrapperGeneratorSupplier;
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
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedGetItemResponse)
					.decoderMethod("mapGetItemResponse")
					.requestClass(GetItemRequest.class)
					.responseClass(GetItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
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
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedBatchGetItemResponse)
					.decoderMethod("mapBatchGetItemResponse")
					.requestClass(BatchGetItemRequest.class)
					.responseClass(BatchGetItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
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
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedPutItemResponse)
					.decoderMethod("mapPutItemResponse")
					.requestClass(PutItemRequest.class)
					.responseClass(PutItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("putItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateUpdateWrappers() {
		for (Method method : overridableMethods(clientClass(), "updateItem")) {
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedUpdateItemResponse)
					.decoderMethod("mapUpdateItemResponse")
					.requestClass(UpdateItemRequest.class)
					.responseClass(UpdateItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("updateItemRequestFromKeyObject")
					.generate());
		}
	}

	protected void generateDeleteWrappers() {
		for (Method method : overridableMethods(clientClass(), "deleteItem")) {
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedDeleteItemResponse)
					.decoderMethod("mapDeleteItemResponse")
					.requestClass(DeleteItemRequest.class)
					.responseClass(DeleteItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
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
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedScanResponse)
					.decoderMethod("mapScanResponse")
					.requestClass(ScanRequest.class)
					.responseClass(ScanResponse.class)
					.generate();
			mapper.addMethod(delegate);
		}
	}

	protected void generateQueryWrappers() {
		for (Method method : overridableMethods(clientClass(), "query")) {
			var delegate = delegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedQueryResponse)
					.decoderMethod("mapQueryResponse")
					.requestClass(QueryRequest.class)
					.responseClass(QueryResponse.class)
					.generate();
			mapper.addMethod(delegate);
		}
	}

	protected void generateBuilder() {
		mapper.addMethod(MethodSpec.methodBuilder("builder")
				.addModifiers(Modifier.PUBLIC)
				.addModifiers(Modifier.STATIC)
				.returns(builderClass())
				.addStatement("return new $T()", builderClass())
				.build());
	}
}
