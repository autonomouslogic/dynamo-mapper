package com.autonomouslogic.dynamomapper.util;

import com.autonomouslogic.dynamomapper.annotations.DynamoPrimaryKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
import com.autonomouslogic.dynamomapper.function.TableNameDecorator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReflectionUtil {
	private final Map<Class<?>, List<String>> primaryKeyCache = new ConcurrentHashMap<>();
	private final Map<Class<?>, String> tableNameCache = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper;
	private final TableNameDecorator tableNameDecorator;

	public List<String> resolvePrimaryKeyFields(Class clazz) {
		return primaryKeyCache.computeIfAbsent(clazz, ignore -> {
			var properties = getProperties(clazz);
			var primaryKeyFields = new ArrayList<String>();
			for (PropertyWriter property : properties) {
				var member = property.getMember();
				var primaryKey = member.getAnnotation(DynamoPrimaryKey.class);
				if (primaryKey != null) {
					primaryKeyFields.add(property.getName());
				}
			}
			return Collections.unmodifiableList(primaryKeyFields);
		});
	}

	public List<PropertyWriter> getProperties(Class<?> clazz) {
		try {
			var provider = objectMapper.getSerializerProviderInstance();
			var serializer = provider.findTypedValueSerializer(clazz, true, null);
			var properties = new ArrayList<PropertyWriter>();
			serializer.properties().forEachRemaining(properties::add);
			return properties;
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
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
