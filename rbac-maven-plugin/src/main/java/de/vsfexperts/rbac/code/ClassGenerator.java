package de.vsfexperts.rbac.code;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

/**
 * Generate the source code of a class containing the fields of the supplier.
 */
public class ClassGenerator {

	private final String packageName;
	private final String className;
	private Supplier<List<FieldSpec>> fieldSupplier;

	public ClassGenerator(final String packageName, final String className) {
		this.packageName = packageName;
		this.className = className;
	}

	public ClassGenerator withFields(final Supplier<List<FieldSpec>> fieldSupplier) {
		this.fieldSupplier = fieldSupplier;
		return this;
	}

	public void generateIn(final File directory) throws IOException {

		final List<FieldSpec> fields = fieldSupplier.get();
		final TypeSpec javaClass = createClass(className, fields);
		final JavaFile javaFile = JavaFile.builder(packageName, javaClass).build();

		javaFile.writeTo(directory);
	}

	private static TypeSpec createClass(final String className, final List<FieldSpec> fields) {
		final Builder classBuilder = TypeSpec //
				.classBuilder(className) //
				.addModifiers(PUBLIC, FINAL);

		fields.stream().forEach(classBuilder::addField);

		return classBuilder.build();
	}

}
