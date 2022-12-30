package com.autonomouslogic.dynamomapper.codegen.util;

import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.integrationTestObject;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

public class TestGenerationHelper {
	public void standardTestMethod(MethodSpec.Builder builder) {
		builder.addAnnotation(ClassName.get("org.junit.jupiter.api", "Test")).addAnnotation(SneakyThrows.class);
	}

	public void standardParameterizedTestMethod(MethodSpec.Builder builder) {
		builder.addAnnotation(ClassName.get("org.junit.jupiter.params", "ParameterizedTest"))
				.addAnnotation(
						AnnotationSpec.builder(ClassName.get("org.junit.jupiter.params.provider", "MethodSource"))
								.addMember(
										"value",
										"\"$L\"",
										"com.autonomouslogic.dynamomapper.test.IntegrationTestUtil#loadIntegrationTestObjects")
								.build())
				.addAnnotation(SneakyThrows.class)
				.addParameter(integrationTestObject, "obj");
	}

	public String testMethodName(Method source) {
		var name = new StringBuilder();
		name.append("test");
		name.append(StringUtils.capitalize(source.getName()));
		name.append("_");
		name.append(Stream.of(source.getParameterTypes())
				.map(c -> c.getSimpleName())
				.collect(Collectors.joining("_")));
		return name.toString();
	}
}
