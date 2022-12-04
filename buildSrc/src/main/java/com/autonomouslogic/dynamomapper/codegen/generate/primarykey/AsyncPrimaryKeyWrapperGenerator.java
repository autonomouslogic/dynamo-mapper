package com.autonomouslogic.dynamomapper.codegen.generate.primarykey;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
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
public class AsyncPrimaryKeyWrapperGenerator extends PrimaryKeyWrapperGenerator {
	public MethodSpec generate() {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(methodNameFactory.create(method, "FromPrimaryKey", multiple))
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		if (!futureWrap) {
			wrapper.addException(IOException.class);
		}
		// Add parameters.
		if (!multiple) {
			wrapper.addParameter(Object.class, "primaryKey");
		} else {
			var type = TypeHelper.genericWildcard(ClassName.get(List.class));
			wrapper.addParameter(type, "primaryKey");
		}
		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals(REQUEST));
		wrapper.addParameters(params);
		// Write body.
		var requestFactoryCode = CodeBlock.builder();
		requestFactoryCode.add("var builder = requestFactory.$L(primaryKey, clazz);\n", factoryMethodName);
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			requestFactoryCode.addStatement("\tconsumer.accept(builder)");
		}
		requestFactoryCode.add(CodeBlock.of("\treturn $L(builder.build(), clazz);\n", method.name));

		var code = CodeBlock.builder();
		if (futureWrap) {
			code.add(CodeBlock.of(
					"return $T.wrapFuture(() -> {\n" + "\t$L\n" + "});",
					TypeHelper.futureUtil,
					requestFactoryCode.build()));
		} else {
			code.add(requestFactoryCode.build());
		}

		wrapper.addCode(code.build());

		TypeHelper.nonNullParameters(wrapper);
		return wrapper.build();
	}
}
