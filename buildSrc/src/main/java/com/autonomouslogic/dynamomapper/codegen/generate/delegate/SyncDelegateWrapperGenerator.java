package com.autonomouslogic.dynamomapper.codegen.generate.delegate;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.CONSUMER;
import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.CLASS_T;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

public class SyncDelegateWrapperGenerator extends DelegateWrapperGenerator {
	public MethodSpec generate() {
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
		// mapper.addMethod(built);
		return built;
	}
}
