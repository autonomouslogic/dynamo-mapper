package com.autonomouslogic.dynamomapper.codegen.util;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import lombok.NonNull;

public class TypeHelper {
	public static final String PACKAGE_NAME = "com.autonomouslogic.dynamomapper";

	public static final ClassName dynamoDecoder = mapperType("codec", "DynamoDecoder");
	public static final ClassName dynamoEncoder = mapperType("codec", "DynamoEncoder");
	public static final ClassName checkedFunction = mapperType("function", "CheckedFunction");
	public static final ClassName reflectionUtil = mapperType("util", "ReflectionUtil");
	public static final ClassName futureUtil = mapperType("util", "FutureUtil");
	public static final ClassName requestFactory = mapperType("request", "RequestFactory");
	public static final ClassName mappedDeleteItemResponse = mapperType("model", "MappedDeleteItemResponse");
	public static final ClassName mappedScanResponse = mapperType("model", "MappedScanResponse");
	public static final ClassName mappedQueryResponse = mapperType("model", "MappedQueryResponse");
	public static final ClassName mappedGetItemResponse = mapperType("model", "MappedGetItemResponse");
	public static final ClassName mappedBatchGetItemResponse = mapperType("model", "MappedBatchGetItemResponse");
	public static final ClassName mappedPutItemResponse = mapperType("model", "MappedPutItemResponse");
	public static final ClassName mappedUpdateItemResponse = mapperType("model", "MappedUpdateItemResponse");
	public static final ClassName dynamoMapperBuilder = mapperType("DynamoMapperBuilder");
	public static final ClassName dynamoAsyncMapperBuilder = mapperType("DynamoAsyncMapperBuilder");

	public static final TypeVariableName T = TypeVariableName.get("T");

	public static final ParameterizedTypeName CLASS_T = ParameterizedTypeName.get(ClassName.get(Class.class), T);

	public static ClassName mapperType(String className) {
		return ClassName.get(PACKAGE_NAME, className);
	}

	public static ClassName mapperType(String subPackage, String className) {
		var fullPackage = PACKAGE_NAME + "." + subPackage;
		return ClassName.get(fullPackage, className);
	}

	public static FieldSpec field(Class clazz, String value) {
		return FieldSpec.builder(clazz, value)
				.addModifiers(Modifier.PRIVATE)
				.addModifiers(Modifier.FINAL)
				.build();
	}

	public static FieldSpec field(ClassName className, String value) {
		return FieldSpec.builder(className, value).addModifiers(Modifier.FINAL).build();
	}

	public static List<Method> overridableMethods(Class<?> clazz, String methodName) {
		return Stream.of(clazz.getMethods())
				.filter(m -> m.getName().equals(methodName))
				.filter(m -> !isStatic(m.getModifiers()))
				.filter(m -> isPublic(m.getModifiers()))
				.sorted(new MethodOrdering())
				.collect(Collectors.toList());
	}

	public static ParameterizedTypeName genericWildcard(ClassName type) {
		return ParameterizedTypeName.get(type, WildcardTypeName.subtypeOf(TypeName.OBJECT));
	}

	public static ParameterizedTypeName genericCapture(ClassName type) {
		return ParameterizedTypeName.get(type, TypeVariableName.get("T"));
	}

	public static ParameterizedTypeName futureGenericCapture(ClassName type) {
		return ParameterizedTypeName.get(ClassName.get(CompletableFuture.class), genericCapture(type));
	}

	public static MethodSpec.Builder nonNullParameters(MethodSpec.Builder builder) {
		var params = builder.parameters;
		for (int i = 0; i < params.size(); i++) {
			var method = params.get(i).toBuilder();
			var isNonNull = method.annotations.stream().anyMatch(a -> a.type.equals(ClassName.get(NonNull.class)));
			if (!isNonNull) {
				method.addAnnotation(NonNull.class);
				params.set(i, (method.build()));
			}
		}
		return builder;
	}
}
