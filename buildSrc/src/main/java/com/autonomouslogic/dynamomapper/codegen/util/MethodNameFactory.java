package com.autonomouslogic.dynamomapper.codegen.util;

import com.squareup.javapoet.MethodSpec;

public class MethodNameFactory {
	public String create(MethodSpec method, String suffix, boolean plural) {
		String methodName = method.name + suffix;
		if (plural) {
			methodName += "s";
		}
		return methodName;
	}
}
