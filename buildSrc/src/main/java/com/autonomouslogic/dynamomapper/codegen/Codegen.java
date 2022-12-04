package com.autonomouslogic.dynamomapper.codegen;

import com.autonomouslogic.dynamomapper.codegen.generate.AsyncSyncMapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.delegate.AsyncDelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.delegate.PaginatorDelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.delegate.SyncDelegateWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.keyobject.AsyncKeyObjectWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.keyobject.SyncKeyObjectWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.primarykey.AsyncPrimaryKeyWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.primarykey.SyncPrimaryKeyWrapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.util.TypeHelper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.FileWriter;
import java.nio.file.Path;
import javax.lang.model.element.Modifier;
import lombok.SneakyThrows;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

public class Codegen extends DefaultTask {

	private Logger log;

	@TaskAction
	@SneakyThrows
	public void run() {
		log = getLogger();
		var mainDir = getProject()
				.getProjectDir()
				.toPath()
				.resolve("src")
				.resolve("main")
				.resolve("java");
		var integrationTestDir = getProject()
				.getProjectDir()
				.toPath()
				.resolve("src")
				.resolve("integrationTest")
				.resolve("java");
		// Init.
		var syncMapper = TypeSpec.classBuilder("DynamoMapper").addModifiers(Modifier.PUBLIC);
		var asyncMapper = TypeSpec.classBuilder("DynamoAsyncMapper").addModifiers(Modifier.PUBLIC);
		var syncMapperTest = TypeSpec.classBuilder("DynamoMapperIntegrationTest");
		var asyncMapperTest = TypeSpec.classBuilder("DynamoAsyncMapperIntegrationTest");
		// Generate.
		new SyncMapperGenerator(
						syncMapper,
						syncMapperTest,
						log,
						SyncDelegateWrapperGenerator::new,
						SyncPrimaryKeyWrapperGenerator::new,
						SyncKeyObjectWrapperGenerator::new)
				.generate();
		new AsyncSyncMapperGenerator(
						asyncMapper,
						asyncMapperTest,
						log,
						AsyncDelegateWrapperGenerator::new,
						AsyncPrimaryKeyWrapperGenerator::new,
						AsyncKeyObjectWrapperGenerator::new,
						PaginatorDelegateWrapperGenerator::new)
				.generate();
		// Write.
		writeType(mainDir, TypeHelper.PACKAGE_NAME, syncMapper.build());
		writeType(mainDir, TypeHelper.PACKAGE_NAME, asyncMapper.build());
		writeType(integrationTestDir, TypeHelper.PACKAGE_NAME, syncMapperTest.build());
		writeType(integrationTestDir, TypeHelper.PACKAGE_NAME, asyncMapperTest.build());
	}

	@SneakyThrows
	private void writeType(Path dir, String packageName, TypeSpec type) {
		var javaFile = JavaFile.builder(packageName, type).indent("\t").build();
		var file = pathForClass(dir, packageName, type.name);
		log.info(String.format("Writing to %s", file));
		try (var out = new FileWriter(file.toFile())) {
			out.write("// This is a generated file, do not edit manually.\n");
			javaFile.writeTo(out);
		}
	}

	private Path pathForClass(Path dir, String packageName, String className) {
		var packageParts = packageName.split("\\.");
		var file = Path.of(dir.toString(), packageParts);
		file = file.resolve(className + ".java");
		return file;
	}
}
