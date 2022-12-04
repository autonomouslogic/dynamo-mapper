package com.autonomouslogic.dynamomapper.codegen;

import com.autonomouslogic.dynamomapper.codegen.generate.AsyncSyncMapperGenerator;
import com.autonomouslogic.dynamomapper.codegen.generate.SyncMapperGenerator;
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
	private Path srcDir;
	private TypeSpec.Builder mapper;
	private TypeSpec.Builder asyncMapper;

	@TaskAction
	@SneakyThrows
	public void run() {
		log = getLogger();
		srcDir = getProject()
				.getProjectDir()
				.toPath()
				.resolve("src")
				.resolve("main")
				.resolve("java");
		// Init.
		mapper = TypeSpec.classBuilder("DynamoMapper").addModifiers(Modifier.PUBLIC);
		asyncMapper = TypeSpec.classBuilder("DynamoAsyncMapper").addModifiers(Modifier.PUBLIC);
		// Generate.
		new SyncMapperGenerator(mapper, log, SyncPrimaryKeyWrapperGenerator::new).generate();
		new AsyncSyncMapperGenerator(asyncMapper, log, AsyncPrimaryKeyWrapperGenerator::new).generate();
		// Write.
		writeType(TypeHelper.PACKAGE_NAME, mapper.build());
		writeType(TypeHelper.PACKAGE_NAME, asyncMapper.build());
	}

	@SneakyThrows
	private void writeType(String packageName, TypeSpec type) {
		var javaFile = JavaFile.builder(packageName, type).indent("\t").build();
		var file = pathForClass(packageName, type.name);
		log.info(String.format("Writing to %s", file));
		try (var out = new FileWriter(file.toFile())) {
			out.write("// This is a generated file, do not edit manually.\n");
			javaFile.writeTo(out);
		}
	}

	private Path pathForClass(String packageName, String className) {
		var packageParts = packageName.split("\\.");
		var file = Path.of(srcDir.toString(), packageParts);
		file = file.resolve(className + ".java");
		return file;
	}
}
