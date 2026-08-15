package com.autonomouslogic.dynamomapper.util;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.introspect.BeanPropertyDefinition;

@RequiredArgsConstructor
public class ReflectionUtil {
	private final Map<Class<?>, List<String>> primaryKeyCache = new ConcurrentHashMap<>();
	private final Map<Class<?>, String> tableNameCache = new ConcurrentHashMap<>();
	private final JsonMapper jsonMapper;
	private final TableNameDecorator tableNameDecorator;

	public List<String> resolvePrimaryKeyFields(Class clazz) {
		return primaryKeyCache.computeIfAbsent(clazz, ignore -> {
			var properties = getProperties(clazz);
			var primaryKeyFields = new ArrayList<String>();
			for (BeanPropertyDefinition property : properties) {
				var member = property.getPrimaryMember();
				var primaryKey = member.getAnnotation(DynamoPrimaryKey.class);
				if (primaryKey != null) {
					primaryKeyFields.add(property.getName());
				}
			}
			return Collections.unmodifiableList(primaryKeyFields);
		});
	}

	public List<BeanPropertyDefinition> getProperties(Class<?> clazz) {
		var config = jsonMapper.serializationConfig();
		var introspector = config.classIntrospectorInstance().forOperation(config);
		var type = jsonMapper.constructType(clazz);
		var annotatedClass = introspector.introspectClassAnnotations(type);
		var beanDesc = introspector.introspectForSerialization(type, annotatedClass);
		return beanDesc.findProperties();
	}

	public String resolveTableName(Class<?> clazz) {
		return tableNameCache.computeIfAbsent(clazz, ignore -> {
			var tableName = clazz.getAnnotation(DynamoTableName.class);
			if (tableName == null) {
				throw new IllegalArgumentException(
						String.format("Class %s is not annotated with @DynamoTableName", clazz.getSimpleName()));
			}
			var name = tableName.value();
			if (tableNameDecorator != null) {
				name = tableNameDecorator.apply(clazz, name);
			}
			return name;
		});
	}
}
