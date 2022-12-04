package com.autonomouslogic.dynamomapper.codegen.generate.primarykey;

import com.autonomouslogic.dynamomapper.codegen.generate.MethodGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.MethodNameFactory;
import com.squareup.javapoet.MethodSpec;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public abstract class PrimaryKeyWrapperGenerator implements MethodGenerator {
	protected final MethodNameFactory methodNameFactory = new MethodNameFactory();

	@NonNull
	protected MethodSpec method;

	@NonNull
	protected String factoryMethodName;

	protected boolean multiple = false;
	protected boolean futureWrap = false;
}
