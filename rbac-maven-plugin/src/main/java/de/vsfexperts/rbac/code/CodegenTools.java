package de.vsfexperts.rbac.code;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;

/**
 * Utilities in order to create common parts of a class (e.g. static constant)
 */
public final class CodegenTools {

	private CodegenTools() {
		// do not instantiate
	}

	public static FieldSpec createConstant(final String name, final Object value) {
		final ClassName targetClass = ClassName.get(value.getClass());

		return FieldSpec //
				.builder(targetClass, name, PUBLIC, STATIC, FINAL) //
				.initializer("$S", value) //
				.build();
	}

}
