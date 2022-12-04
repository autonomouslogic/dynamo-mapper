package com.autonomouslogic.dynamomapper.codegen.generate.keyobject;

import com.autonomouslogic.dynamomapper.codegen.generate.MethodGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.MethodNameFactory;
import com.squareup.javapoet.MethodSpec;
import lombok.Data;

@Data
public abstract class KeyObjectWrapperGenerator implements MethodGenerator {
	protected final MethodNameFactory methodNameFactory = new MethodNameFactory();
	protected MethodSpec method;
	protected String factoryMethodName;
	protected boolean multiple;
	protected boolean futureWrap;
}
