package com.autonomouslogic.dynamomapper.codegen.generate.delegate;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.CONSUMER;
import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.CLASS_T;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

public class AsyncDelegateWrapperGenerator extends DelegateWrapperGenerator {
	public MethodSpec generate() {
		var innerReturnType = ParameterizedTypeName.get(returnType, TypeHelper.T);
		var outerReturnType = TypeHelper.futureGenericCapture(returnType);
		return internalGenerateDelegateWrapper(
				method, outerReturnType, innerReturnType, decoderMethod, requestClass, responseClass, "thenApply");
	}

	protected MethodSpec internalGenerateDelegateWrapper(
			Method method,
			TypeName outerReturnType,
			TypeName innerReturnType,
			String decoderMethod,
			Class<?> requestClass,
			Class<?> responseClass,
			String callbackMethod) {
		var requestVar = detectRequestOrConsumer(method);
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(method.getName())
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(outerReturnType);
		wrapper.addExceptions(
				Stream.of(method.getExceptionTypes()).map(e -> ClassName.get(e)).collect(Collectors.toList()));
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
		wrapper.addStatement(CodeBlock.of(
				"return client.$L(reqOrConsumer)\n" + "\t.$L(new $T<>() {\n"
						+ "\t\t@Override\n"
						+ "\t\tpublic $T checkedApply($T response) throws $T {\n"
						+ "\t\t\treturn decoder.$L(response, clazz);\n"
						+ "\t\t}\n"
						+ "\t})",
				method.getName(),
				callbackMethod,
				TypeHelper.checkedFunction,
				innerReturnType,
				responseClass,
				Exception.class,
				decoderMethod));

		TypeHelper.nonNullParameters(wrapper);
		var built = wrapper.build();
		// mapper.addMethod(built);
		return built;
	}
}
