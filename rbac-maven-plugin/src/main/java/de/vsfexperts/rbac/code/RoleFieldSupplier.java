package de.vsfexperts.rbac.code;

import static de.vsfexperts.rbac.code.CodegenTools.createConstant;
import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getRoleNames;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import com.squareup.javapoet.FieldSpec;

/**
 * Provides a list of fields (public static String name = "name") for roles
 */
public class RoleFieldSupplier implements Supplier<List<FieldSpec>> {

	private final Set<RoleMapping> mapping;

	public RoleFieldSupplier(final Set<RoleMapping> mapping) {
		this.mapping = mapping;
	}

	@Override
	public List<FieldSpec> get() {
		return getRoleNames(mapping).stream().map(this::createFieldSpec).collect(Collectors.toList());
	}

	private FieldSpec createFieldSpec(final String name) {
		return createConstant(name, name);
	}

}
