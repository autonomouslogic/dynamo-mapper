package com.autonomouslogic.dynamomapper.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.field;
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
			.addModifiers(Modifier.PUBLIC);
		var client = ParameterSpec.builder(clientField.type, "client")
			.addAnnotation(NonNull.class)
			.build();
		var objectMapper = ParameterSpec.builder(ObjectMapper.class, "objectMapper")
			.addAnnotation(NonNull.class)
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
			generateDelegateGetWrapper(method, TypeHelper.mappedGetItemResponse, "mapGetItemResponse");
		}
	}

	protected void generatePutWrappers() {
		for (Method method : overridableMethods(DynamoDbClient.class, "putItem")) {
			generateDelegateGetWrapper(method, TypeHelper.mappedPutItemResponse, "mapPutItemResponse");
		}
	}

	protected void generateDeleteWrappers() {
		for (Method method : overridableMethods(DynamoDbClient.class, "deleteItem")) {
			generateDelegateGetWrapper(method, TypeHelper.mappedDeleteItemResponse, "mapDeleteItemResponse");
		}
	}

	protected void generateDelegateGetWrapper(Method method, ClassName returnType, String decoderMethod) {
		var wrapper = MethodSpec.methodBuilder(method.getName())
			.addModifiers(Modifier.PUBLIC);
		wrapper.addExceptions(List.of(method.getExceptionTypes())
			.stream()
			.map(e -> ClassName.get(e))
			.collect(Collectors.toList())
		);
		wrapper.addException(JsonProcessingException.class);

		var delegateParams = List.of(method.getGenericParameterTypes());
		if (delegateParams.size() != 1) {
			throw new IllegalArgumentException(String.format("Delegate param generation only supports 1 param, %s seen for %s",
				delegateParams.size(), method.getName()));
		}
		wrapper.addParameter(delegateParams.get(0), "request");
		wrapper.addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(TypeName.OBJECT)), "clazz");
		wrapper.addStatement("return decoder.$L(client.$L(request), clazz)",
			decoderMethod,
			method.getName());
		wrapper.returns(ParameterizedTypeName.get(returnType, WildcardTypeName.subtypeOf(TypeName.OBJECT)));
		mapper.addMethod(wrapper.build());

	}

	private String paramName(Type type) {
		if (type instanceof ParameterizedType) {
			return paramName(((ParameterizedType) type).getOwnerType());
		}
		return StringUtils.uncapitalize(type.getTypeName());
	}
}
