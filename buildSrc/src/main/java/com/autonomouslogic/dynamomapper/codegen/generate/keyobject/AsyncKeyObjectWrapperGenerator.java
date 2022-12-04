package com.autonomouslogic.dynamomapper.codegen.generate.keyobject;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AsyncKeyObjectWrapperGenerator extends KeyObjectWrapperGenerator {
	public MethodSpec generate() {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(methodNameFactory.create(method, "FromKeyObject", multiple))
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		if (!futureWrap) {
			wrapper.addException(IOException.class);
		}
		wrapper.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
				.addMember("value", "\"unchecked\"")
				.build());
		// Add parameters.
		if (!multiple) {
			wrapper.addParameter(Object.class, "keyObject");
		} else {
			var type = TypeHelper.genericWildcard(ClassName.get(List.class));
			wrapper.addParameter(type, "keyObject");
		}
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals(REQUEST));
		params.removeIf(p -> p.name.equals("clazz"));
		wrapper.addParameters(params);
		// Write body.
		var requestFactoryCode = CodeBlock.builder();
		var firstParamTypeName = method.parameters.get(0).type;
		requestFactoryCode.add("var builder = requestFactory.$L(keyObject);\n", factoryMethodName);
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			requestFactoryCode.addStatement("\tconsumer.accept(builder)");
		}
		requestFactoryCode.add(
				CodeBlock.of("return $L(builder.build(), (Class<T>) keyObject.getClass());\n", method.name));

		var code = CodeBlock.builder();
		if (futureWrap) {
			code.add(CodeBlock.of("return $T.wrapFuture(() -> {\n" + "\t$L\n" + "});", requestFactoryCode.build()));
		} else {
			code.add(requestFactoryCode.build());
		}
		wrapper.addCode(code.build());

		TypeHelper.nonNullParameters(wrapper);
		return wrapper.build();
	}
}
