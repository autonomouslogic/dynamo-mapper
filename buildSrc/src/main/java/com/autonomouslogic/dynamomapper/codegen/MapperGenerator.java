package com.autonomouslogic.dynamomapper.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.field;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.CLASS_T;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedDeleteItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedPutItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.overridableMethods;

@RequiredArgsConstructor
public class MapperGenerator {
	private final TypeSpec.Builder mapper;
	private final Logger log;

	private FieldSpec clientField;
	private FieldSpec encoderField;
	private FieldSpec decoderField;
	private FieldSpec requestFactoryField;
	private FieldSpec reflectionUtilField;

	public void generate() {
		generateFields();
		generateConstructor();
		generateGetWrappers();
		generatePutWrappers();
		generateDeleteWrappers();
		generateBuilder();
	}

	protected void generateFields() {
		clientField = field(DynamoDbClient.class, "client");
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
		var constructor = MethodSpec.constructorBuilder()
			.addModifiers(Modifier.PROTECTED);
		var client = ParameterSpec.builder(clientField.type, "client")
			.build();
		var objectMapper = ParameterSpec.builder(ObjectMapper.class, "objectMapper")
			.build();
		constructor.addParameter(client);
		constructor.addParameter(objectMapper);

		constructor
			.addStatement("this.client = client")
			.addStatement("encoder = new $T(objectMapper)", encoderField.type)
			.addStatement("decoder = new $T(objectMapper)", decoderField.type)
			.addStatement("reflectionUtil = new $T(objectMapper)", reflectionUtilField.type)
			.addStatement("requestFactory = new $T(encoder, objectMapper, reflectionUtil)", requestFactoryField.type);

		mapper.addMethod(constructor.build());
	}

	protected void generateGetWrappers() {
		for (Method method : overridableMethods(DynamoDbClient.class, "getItem")) {
			var delegate = generateDelegateWrapper(method, mappedGetItemResponse, "mapGetItemResponse");
			generateHashKeyWrapper(delegate, "getRequestFromHashKey");
			generateKeyObjectWrapper(delegate, "getRequestFromKeyObject");
		}
	}

	protected void generatePutWrappers() {
		for (Method method : overridableMethods(DynamoDbClient.class, "putItem")) {
			var delegate = generateDelegateWrapper(method, mappedPutItemResponse, "mapPutItemResponse");
			generateKeyObjectWrapper(delegate, "putRequestFromObject");
		}
	}

	protected void generateDeleteWrappers() {
		for (Method method : overridableMethods(DynamoDbClient.class, "deleteItem")) {
			var delegate = generateDelegateWrapper(method, mappedDeleteItemResponse, "mapDeleteItemResponse");
			generateHashKeyWrapper(delegate, "deleteRequestFromHashKey");
			generateKeyObjectWrapper(delegate, "deleteRequestFromKeyObject");
		}
	}

	protected MethodSpec generateDelegateWrapper(Method method, ClassName returnType, String decoderMethod) {
		// Detect consumer.
		var requestVar = "request";
		var firstParamTypeName = method.getParameterTypes()[0];
		if (firstParamTypeName.equals(Consumer.class)) {
			requestVar = "consumer";
		}
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.getName())
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(TypeHelper.genericCapture(returnType));
		wrapper.addExceptions(Stream.of(method.getExceptionTypes())
			.map(e -> ClassName.get(e))
			.collect(Collectors.toList())
		);
		wrapper.addException(JsonProcessingException.class);
		// Add parameters.
		var delegateParams = List.of(method.getGenericParameterTypes());
		if (delegateParams.size() != 1) {
			throw new IllegalArgumentException(String.format("Delegate param generation only supports 1 param, %s seen for %s",
				delegateParams.size(), method.getName()));
		}
		wrapper.addParameter(delegateParams.get(0), requestVar);
		wrapper.addParameter(CLASS_T, "clazz");
		// Write body.
		wrapper.addStatement("return decoder.$L(client.$L($L), clazz)",
			decoderMethod,
			method.getName(),
			requestVar);

		TypeHelper.nonNullParameters(wrapper);
		var built = wrapper.build();
		mapper.addMethod(built);
		return built;
	}

	protected void generateHashKeyWrapper(MethodSpec method, String factoryMethodName) {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.name)
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		wrapper.addException(IOException.class);
		// Add parameters.
		wrapper.addParameter(Object.class, "hashKey");
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals("request"));
		wrapper.addParameters(params);
		// Write body.
		wrapper.addStatement("var builder = requestFactory.$L(hashKey, clazz)", factoryMethodName);
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			wrapper.addStatement("consumer.accept(builder)");
		}
		wrapper.addStatement("return $L(builder.build(), clazz)", method.name);

		TypeHelper.nonNullParameters(wrapper);
		mapper.addMethod(wrapper.build());
	}

	protected void generateKeyObjectWrapper(MethodSpec method, String factoryMethodName) {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.name)
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		wrapper.addException(IOException.class);
		wrapper.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
			.addMember("value", "\"unchecked\"")
			.build());
		// Add parameters.
		wrapper.addParameter(Object.class, "keyObject");
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals("request"));
		params.removeIf(p -> p.name.equals("clazz"));
		wrapper.addParameters(params);
		// Write body.
		wrapper.addStatement("var builder = requestFactory.$L(keyObject)", factoryMethodName);
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			wrapper.addStatement("consumer.accept(builder)");
		}
		wrapper.addStatement("return $L(builder.build(), ($T) keyObject.getClass())", method.name, CLASS_T);

		TypeHelper.nonNullParameters(wrapper);
		mapper.addMethod(wrapper.build());
	}

	private void generateBuilder() {
		mapper.addMethod(MethodSpec.methodBuilder("builder")
			.addModifiers(Modifier.PUBLIC)
			.addModifiers(Modifier.STATIC)
			.returns(TypeHelper.dynamoMapperBuilder)
			.addStatement("return new $T()", TypeHelper.dynamoMapperBuilder)
			.build());
	}
}
