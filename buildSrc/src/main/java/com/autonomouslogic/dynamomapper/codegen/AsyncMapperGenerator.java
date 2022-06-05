package com.autonomouslogic.dynamomapper.codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.CLASS_T;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.field;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedDeleteItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.mappedPutItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.TypeHelper.overridableMethods;

public class AsyncMapperGenerator extends MapperGenerator {
	public AsyncMapperGenerator(TypeSpec.Builder mapper, Logger log) {
		super(mapper, log);
	}

	@Override
	protected Class<?> clientClass() {
		return DynamoDbAsyncClient.class;
	}

	@Override
	protected ClassName builderClass() {
		return TypeHelper.dynamoAsyncMapperBuilder;
	}

	@Override
	protected MethodSpec generateDelegateWrapper(Method method, ClassName returnType, String decoderMethod, Class<?> responseClass) {
		var requestVar = detectRequestOrConsumer(method);
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.getName())
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(TypeHelper.futureGenericCapture(returnType));
		wrapper.addExceptions(Stream.of(method.getExceptionTypes())
			.map(e -> ClassName.get(e))
			.collect(Collectors.toList())
		);
		// Add parameters.
		var delegateParams = List.of(method.getGenericParameterTypes());
		if (delegateParams.size() != 1) {
			throw new IllegalArgumentException(String.format("Delegate param generation only supports 1 param, %s seen for %s",
				delegateParams.size(), method.getName()));
		}
		wrapper.addParameter(delegateParams.get(0), requestVar);
		wrapper.addParameter(CLASS_T, "clazz");
		// Write body.
		wrapper.addStatement(CodeBlock.of(
			"return client.$L($L)\n" +
			"\t.thenApply(new $T<>() {\n" +
			"\t\t@Override\n" +
			"\t\tpublic $T checkedApply($T response) throws $T {\n" +
			"\t\t\treturn decoder.$L(response, clazz);\n" +
			"\t\t}\n" +
			"\t})"
			, method.getName(), requestVar, TypeHelper.checkedFunction, TypeHelper.genericCapture(returnType),
			responseClass, Exception.class, decoderMethod));

		TypeHelper.nonNullParameters(wrapper);
		var built = wrapper.build();
		mapper.addMethod(built);
		return built;
	}

	@Override
	protected void generateHashKeyWrapper(MethodSpec method, String factoryMethodName) {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.name)
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		// Add parameters.
		wrapper.addParameter(Object.class, "hashKey");
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals("request"));
		wrapper.addParameters(params);
		// Write body.
		var code = CodeBlock.builder();
		code.add(CodeBlock.of(
			"return $T.wrapFuture(() -> {\n" +
			"\tvar builder = requestFactory.$L(hashKey, clazz);\n",
			TypeHelper.futureUtil, factoryMethodName
		));
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			code.addStatement("\tconsumer.accept(builder)");
		}
		code.add(CodeBlock.of(
			"\treturn $L(builder.build(), clazz);\n" +
			"});", method.name));
		wrapper.addCode(code.build());

		TypeHelper.nonNullParameters(wrapper);
		mapper.addMethod(wrapper.build());
	}

	@Override
	protected void generateKeyObjectWrapper(MethodSpec method, String factoryMethodName) {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.name)
			.addModifiers(Modifier.PUBLIC)
			.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		wrapper.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
			.addMember("value", "\"unchecked\"")
			.build());
		// Add parameters.
		wrapper.addParameter(TypeHelper.T, "keyObject");
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals("request"));
		params.removeIf(p -> p.name.equals("clazz"));
		wrapper.addParameters(params);
		// Write body.
		var code = CodeBlock.builder();
		code.add(CodeBlock.of(
			"return $T.wrapFuture(() -> {\n" +
				"\tvar builder = requestFactory.$L(keyObject);\n",
			TypeHelper.futureUtil, factoryMethodName
		));
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			code.addStatement("\tconsumer.accept(builder)");
		}
		code.add(CodeBlock.of(
			"\treturn $L(builder.build(), (Class<T>) keyObject.getClass());\n" +
				"});", method.name));
		wrapper.addCode(code.build());

		TypeHelper.nonNullParameters(wrapper);
		mapper.addMethod(wrapper.build());
	}
}
