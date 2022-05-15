package com.autonomouslogic.dynamomapper.codegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.lang.model.element.Modifier;

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
	}

	protected void generateFields() {
		clientField = field(DynamoDbClient.class, "client");
		mapper.addField(clientField);
		encoderField = field("codec", "DynamoEncoder", "encoder");
		mapper.addField(encoderField);
		decoderField = field("codec", "DynamoDecoder", "decoder");
		mapper.addField(decoderField);
		requestFactoryField = field("request", "RequestFactory", "requestFactory");
		mapper.addField(requestFactoryField);
		reflectionUtilField = field("util", "ReflectionUtil", "reflectionUtil");
		mapper.addField(reflectionUtilField);
	}

	protected FieldSpec field(Class clazz, String value) {
		return FieldSpec.builder(clazz, value)
			.addModifiers(Modifier.FINAL)
			.build();
	}

	protected FieldSpec field(String subPackage, String className, String value) {
		var fullPackage = Codegen.PACKAGE_NAME + "." + subPackage;
		var typeName = ClassName.get(fullPackage, className);
		return FieldSpec.builder(typeName, value)
			.addModifiers(Modifier.FINAL)
			.build();
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
}
