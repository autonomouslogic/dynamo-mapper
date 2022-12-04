package com.autonomouslogic.dynamomapper.codegen.generate.keyobject;

import static com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator.REQUEST;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.CLASS_T;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.AnnotationSpec;
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
public class SyncKeyObjectWrapperGenerator extends KeyObjectWrapperGenerator {
	public MethodSpec generate() {
		// Create signature.
		var wrapper = MethodSpec.methodBuilder(methodNameFactory.create(method, "FromKeyObject", multiple))
				.addModifiers(Modifier.PUBLIC)
				.addTypeVariable(TypeHelper.T);
		wrapper.returns(method.returnType);
		wrapper.addExceptions(method.exceptions);
		wrapper.addException(IOException.class);
		wrapper.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
				.addMember("value", "\"unchecked\"")
				.build());
		// Add parameters.
		if (!multiple) {
			wrapper.addParameter(TypeHelper.T, "keyObject");
		} else {
			var type = ParameterizedTypeName.get(ClassName.get(List.class), TypeHelper.T);
			wrapper.addParameter(type, "keyObject");
			wrapper.addParameter(CLASS_T, "clazz");
		}

		var params = new ArrayList<>(method.parameters);
		params.removeIf(p -> p.name.equals(REQUEST));
		params.removeIf(p -> p.name.equals("clazz"));
		wrapper.addParameters(params);
		// Write body.
		wrapper.addStatement("var builder = requestFactory.$L(keyObject)", factoryMethodName);
		var firstParamTypeName = method.parameters.get(0).type;
		if (firstParamTypeName instanceof ParameterizedTypeName) {
			wrapper.addStatement("consumer.accept(builder)");
		}
		if (!multiple) {
			wrapper.addStatement("return $L(builder.build(), ($T) keyObject.getClass())", method.name, CLASS_T);
		} else {
			wrapper.addStatement("return $L(builder.build(), clazz)", method.name);
		}

		TypeHelper.nonNullParameters(wrapper);
		return wrapper.build();
	}
}
