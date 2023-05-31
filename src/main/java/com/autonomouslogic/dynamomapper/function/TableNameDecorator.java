package com.autonomouslogic.dynamomapper.function;

import java.util.function.BiFunction;

public interface TableNameDecorator extends BiFunction<Class<?>, String, String> {}
