package de.vsfexperts.rbac.configuration.domain.util;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toConcurrentMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;

/**
 * Helper methods/functions in order to ease usage of {@link Collection}s of
 * {@link RoleMapping}s
 *
 */
public final class MappingTools {

	private MappingTools() {
		// do not instantiate
	}

	public static Set<String> getPrivilegeNames(final Collection<RoleMapping> mappings) {
		requireNonNull(mappings);
		return mappings.stream().flatMap(m -> m.getPrivilegeNames().stream()).collect(toCollection(TreeSet::new));
	}

	public static Set<String> getRoleNames(final Collection<RoleMapping> mappings) {
		requireNonNull(mappings);
		return mappings.stream().map(RoleMapping::getRoleName).collect(toCollection(TreeSet::new));
	}

	/**
	 * Convert {@link RoleMapping}s to simple Map
	 *
	 * @param mappings to convert
	 * @return map with key = role, value = privileges
	 */
	public static Map<String, Set<String>> asMap(final Collection<RoleMapping> mappings) {
		requireNonNull(mappings);
		return mappings.stream().collect(toConcurrentMap(RoleMapping::getRoleName, RoleMapping::getPrivilegeNames));
	}

}
