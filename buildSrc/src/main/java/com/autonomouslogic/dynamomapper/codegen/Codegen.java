package com.autonomouslogic.dynamomapper.codegen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.SneakyThrows;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;

import java.io.FileWriter;
import java.nio.file.Path;

public class Codegen extends DefaultTask {
	public static final String PACKAGE_NAME = "com.autonomouslogic.dynamomapper";

	private Logger log;
	private Path srcDir;
	private TypeSpec.Builder mapper;

	@TaskAction
	@SneakyThrows
	public void run() {
		log = getLogger();
		srcDir = getProject().getProjectDir().toPath().resolve("src").resolve("main").resolve("java");
		// Init.
		mapper = TypeSpec.classBuilder("DynamoMapper2");
		// Generate.
		new MapperGenerator(mapper, log).generate();
		// Write.
		writeType(PACKAGE_NAME, mapper.build());
	}

	@SneakyThrows
	private void writeType(String packageName, TypeSpec type) {
		var javaFile = JavaFile.builder(packageName, type)
			.indent("\t")
			.build();
		var file = pathForClass(packageName, type.name);
		log.info(String.format("Writing to %s", file));
		try (var out = new FileWriter(file.toFile())) {
			out.write("/*\n This is a generated file, do not edit manually.\n*/\n");
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
