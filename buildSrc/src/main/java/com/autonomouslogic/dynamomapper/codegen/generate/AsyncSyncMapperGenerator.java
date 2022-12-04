package com.autonomouslogic.dynamomapper.codegen.generate;

import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.mappedBatchGetItemResponse;
import static com.autonomouslogic.dynamomapper.codegen.util.TypeHelper.overridableMethods;

import com.autonomouslogic.dynamomapper.codegen.generate.delegate.DelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.delegate.PaginatorDelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.keyobject.KeyObjectWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.primarykey.PrimaryKeyWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import org.gradle.api.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchGetItemResponse;

public class AsyncSyncMapperGenerator extends SyncMapperGenerator {
	private final Supplier<PaginatorDelegateWrapperGenerator> paginatorDelegateWrapperGeneratorSupplier;

	public AsyncSyncMapperGenerator(
			TypeSpec.Builder mapper,
			Logger log,
			Supplier<DelegateWrapperGenerator> delegateWrapperGeneratorSupplier,
			Supplier<PrimaryKeyWrapperGenerator> primaryKeyWrapperGeneratorSupplier,
			Supplier<KeyObjectWrapperGenerator> keyObjectWrapperGeneratorSupplier,
			Supplier<PaginatorDelegateWrapperGenerator> paginatorDelegateWrapperGeneratorSupplier) {
		super(
				mapper,
				log,
				delegateWrapperGeneratorSupplier,
				primaryKeyWrapperGeneratorSupplier,
				keyObjectWrapperGeneratorSupplier);
		this.paginatorDelegateWrapperGeneratorSupplier = paginatorDelegateWrapperGeneratorSupplier;
	}

	@Override
	public void generate() {
		super.generate();
		generateBatchGetPaginatorWrappers();
	}

	@Override
	protected Class<?> clientClass() {
		return DynamoDbAsyncClient.class;
	}

	@Override
	protected ClassName builderClass() {
		return TypeHelper.dynamoAsyncMapperBuilder;
	}

	private void generateBatchGetPaginatorWrappers() {
		for (Method method : overridableMethods(clientClass(), "batchGetItemPaginator")) {
			var delegate = paginatorDelegateWrapperGeneratorSupplier
					.get()
					.method(method)
					.returnType(mappedBatchGetItemResponse)
					.decoderMethod("mapBatchGetItemResponse")
					.requestClass(BatchGetItemRequest.class)
					.responseClass(BatchGetItemResponse.class)
					.generate();
			mapper.addMethod(delegate);
			mapper.addMethod(primaryKeyWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("batchGetItemRequestFromPrimaryKeys")
					.multiple(true)
					.generate());
			mapper.addMethod(keyObjectWrapperGeneratorSupplier
					.get()
					.method(delegate)
					.factoryMethodName("batchGetItemRequestFromKeyObjects")
					.multiple(true)
					.generate());
		}
	}
}
