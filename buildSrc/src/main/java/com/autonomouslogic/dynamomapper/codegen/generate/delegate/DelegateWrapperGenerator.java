package com.autonomouslogic.dynamomapper.codegen.generate.delegate;

import com.autonomouslogic.dynamomapper.codegen.generate.MethodGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.TestGenerationHelper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public abstract class DelegateWrapperGenerator implements MethodGenerator {
	private final TestGenerationHelper testGenerationHelper = new TestGenerationHelper();

	@NonNull
	protected Method method;

	@NonNull
	protected ClassName returnType;

	protected String decoderMethod;

	@NonNull
	protected Class<?> requestClass;

	@NonNull
	protected Class<?> responseClass;

	protected String detectRequestOrConsumer(Method method) {
		var requestVar = SyncMapperGenerator.REQUEST;
		var firstParamTypeName = method.getParameterTypes()[0];
		if (firstParamTypeName.equals(Consumer.class)) {
			requestVar = SyncMapperGenerator.CONSUMER;
		}
		return requestVar;
	}

	protected void generateRequestObjectWrapper(MethodSpec.Builder wrapper, Class<?> requestClass, String requestVar) {
		wrapper.addStatement(
				"var reqOrConsumer = requestFactory.accept$L($L, clazz)", requestClass.getSimpleName(), requestVar);
	}

	protected void generateRequestConsumerWrapper(
			MethodSpec.Builder wrapper, Class<?> requestClass, String requestVar) {
		wrapper.beginControlFlow("Consumer<$T.Builder> reqOrConsumer = (builder) -> {", requestClass)
				.addStatement("requestFactory.accept$L(builder, clazz)", requestClass.getSimpleName())
				.addStatement("$L.accept(builder)", requestVar)
				.endControlFlow("}");
	}

	public MethodSpec generateTest() {
		var builder = MethodSpec.methodBuilder(testGenerationHelper.testMethodName(method));
		testGenerationHelper.standardParameterizedTestMethod(builder);
		return builder.build();
	}
}
