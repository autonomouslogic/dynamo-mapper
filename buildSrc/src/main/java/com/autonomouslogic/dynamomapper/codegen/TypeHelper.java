package com.autonomouslogic.dynamomapper.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public class TypeHelper {
	public static final String PACKAGE_NAME = "com.autonomouslogic.dynamomapper";

	public static final ClassName dynamoDecoder = mapperType("codec", "DynamoDecoder");
	public static final ClassName dynamoEncoder = mapperType("codec", "DynamoEncoder");
	public static final ClassName reflectionUtil = mapperType("util", "ReflectionUtil");
	public static final ClassName requestFactory = mapperType("request", "RequestFactory");
	public static final ClassName mappedDeleteItemResponse = mapperType("model", "MappedDeleteItemResponse");
	public static final ClassName mappedGetItemResponse = mapperType("model", "MappedGetItemResponse");
	public static final ClassName mappedPutItemResponse = mapperType("model", "MappedPutItemResponse");

	public static ClassName mapperType(String subPackage, String className) {
		var fullPackage = PACKAGE_NAME + "." + subPackage;
		return ClassName.get(fullPackage, className);
	}

	public static FieldSpec field(Class clazz, String value) {
		return FieldSpec.builder(clazz, value)
			.addModifiers(Modifier.FINAL)
			.build();
	}

	public static FieldSpec field(ClassName className, String value) {
		return FieldSpec.builder(className, value)
			.addModifiers(Modifier.FINAL)
			.build();
	}

	public static List<Method> overridableMethods(Class<?> clazz, String methodName) {
		return Stream.of(clazz.getMethods())
			.filter(m -> m.getName().equals(methodName))
			.filter(m -> !isStatic(m.getModifiers()))
			.filter(m -> isPublic(m.getModifiers()))
			.collect(Collectors.toList());
	}
}
