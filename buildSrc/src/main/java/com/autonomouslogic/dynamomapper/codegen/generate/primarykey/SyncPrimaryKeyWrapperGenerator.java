package com.autonomouslogic.dynamomapper.codegen.generate.primarykey;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.ClassName;
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
public class SyncPrimaryKeyWrapperGenerator extends PrimaryKeyWrapperGenerator {
	public MethodSpec generate() {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(methodNameFactory.create(method, "FromPrimaryKey", multiple))
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		wrapper.addException(IOException.class);
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
		wrapper.addStatement("var builder = requestFactory.$L(primaryKey, clazz)", factoryMethodName);
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			wrapper.addStatement("consumer.accept(builder)");
		}
		wrapper.addStatement("return $L(builder.build(), clazz)", method.name);

		TypeHelper.nonNullParameters(wrapper);
		return wrapper.build();
	}
}
