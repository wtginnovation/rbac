package de.vsfexperts.rbac.spring.mapping;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Map collections of roles to different output formats
 */
public class RoleMapper {

	private static final String SPRING_ROLE_PREFIX = "ROLE_";

	private final Supplier<Map<String, Set<String>>> mappingSupplier;
	private boolean springRoles;

	public RoleMapper(final Supplier<Map<String, Set<String>>> mappingSupplier) {
		requireNonNull(mappingSupplier);
		this.mappingSupplier = mappingSupplier;
	}

	public Set<GrantedAuthority> mapRolesToGrantedAuthority(final Collection<String> roles) {
		final Function<String, GrantedAuthority> privilegeConverter;
		if (springRoles) {
			privilegeConverter = p -> new SimpleGrantedAuthority(SPRING_ROLE_PREFIX + p);
		} else {
			privilegeConverter = p -> new SimpleGrantedAuthority(p);
		}

		return mapRolesToPrivileges(roles, privilegeConverter);
	}

	public <T> Set<T> mapRolesToPrivileges(final Collection<String> roles, final Function<String, T> converter) {
		final Set<T> allPrivileges = new HashSet<>();
		final Map<String, Set<String>> mapping = mappingSupplier.get();

		for (final String role : roles) {
			final String realRole = normalize(role);
			final Set<String> definedPrivileges = mapping.get(realRole);

			allPrivileges.addAll(convertPrivileges(definedPrivileges, converter));
		}

		return allPrivileges;
	}

	private String normalize(final String role) {
		return role.toUpperCase().replaceFirst(SPRING_ROLE_PREFIX, "");
	}

	private <T> Set<T> convertPrivileges(final Set<String> privileges, final Function<String, T> converter) {
		if (privileges == null || privileges.isEmpty()) {
			return emptySet();
		}

		return privileges.stream().map(converter).collect(toSet());
	}

	public boolean isSpringRoles() {
		return springRoles;
	}

	public void setSpringRoles(final boolean springRoles) {
		this.springRoles = springRoles;
	}

}
