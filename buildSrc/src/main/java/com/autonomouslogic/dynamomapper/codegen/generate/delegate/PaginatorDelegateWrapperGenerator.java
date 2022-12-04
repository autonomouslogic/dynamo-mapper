package com.autonomouslogic.dynamomapper.codegen.generate.delegate;

import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.reactivestreams.Publisher;

public class PaginatorDelegateWrapperGenerator extends AsyncDelegateWrapperGenerator {
	@Override
	public MethodSpec generate() {
		var innerReturnType = ParameterizedTypeName.get(returnType, TypeHelper.T);
		var outerReturnType =
				ParameterizedTypeName.get(com.squareup.javapoet.ClassName.get(Publisher.class), innerReturnType);
		return internalGenerateDelegateWrapper(
				method, outerReturnType, innerReturnType, decoderMethod, requestClass, responseClass, "map");
	}
}
