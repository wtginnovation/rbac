package de.vsfexperts.rbac.code;

import static de.vsfexperts.rbac.code.CodegenTools.createConstant;
import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getPrivilegeNames;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import com.squareup.javapoet.FieldSpec;

/**
 * Provides a list of fields (public static String name = "name") for privileges
 */
public class PrivilegeFieldSupplier implements Supplier<List<FieldSpec>> {

	private static final String SPRING_ROLE_PREFIX = "ROLE_";

	private final Set<RoleMapping> mapping;
	private final boolean springCompatible;

	public PrivilegeFieldSupplier(final Set<RoleMapping> mapping, final boolean springCompatible) {
		this.mapping = mapping;
		this.springCompatible = springCompatible;
	}

	@Override
	public List<FieldSpec> get() {
		return getPrivilegeNames(mapping).stream().map(this::createFieldSpec).collect(Collectors.toList());
	}

	private FieldSpec createFieldSpec(final String name) {
		final String value = createConstantValue(name);

		return createConstant(name, value);
	}

	private String createConstantValue(final String privilege) {
		if (springCompatible) {
			return SPRING_ROLE_PREFIX + privilege;
		} else {
			return privilege;
		}
	}

}
