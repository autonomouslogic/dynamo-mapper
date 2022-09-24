package com.autonomouslogic.dynamomapper.util;


import com.autonomouslogic.dynamomapper.annotations.DynamoHashKey;
import com.autonomouslogic.dynamomapper.annotations.DynamoTableName;
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
	private static final Map<Class<?>, List<String>> hashKeyCache = new ConcurrentHashMap<>();
	private static final Map<Class<?>, String> tableNameCache = new ConcurrentHashMap<>();
	private final ObjectMapper objectMapper;

	public List<String> resolveHashKeyFields(Class clazz) {
		return hashKeyCache.computeIfAbsent(clazz, ignore -> {
			var properties = getProperties(clazz);
			var hashKeyFields = new ArrayList<String>();
			for (PropertyWriter property : properties) {
				var member = property.getMember();
				var hashKey = member.getAnnotation(DynamoHashKey.class);
				if (hashKey != null) {
					hashKeyFields.add(property.getName());
				}
			}
			return Collections.unmodifiableList(hashKeyFields);
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
			return tableName.value();
		});
	}
}
