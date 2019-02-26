package de.vsfexperts.rbac.spring.supplier;

import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.asMap;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.Resource;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import de.vsfexperts.rbac.configuration.io.YamlLoader;

/**
 * Supplier in order to provide content based on configuration in yaml file
 * (e.g. rbac.yaml)
 */
public class YamlMappingSupplier implements RbacMappingSupplier {

	private final Resource mappingLocation;

	public YamlMappingSupplier(final Resource mappingLocation) {
		requireNonNull(mappingLocation, "mapping location must not be null");
		this.mappingLocation = mappingLocation;
	}

	@Override
	public Map<String, Set<String>> get() {
		try {
			return loadMapping();
		} catch (final Exception e) {
			throw new IllegalArgumentException("Couldn't load mapping from " + mappingLocation.getDescription(), e);
		}
	}

	private Map<String, Set<String>> loadMapping() throws IOException {
		final Set<RoleMapping> mapping = new YamlLoader().load(mappingLocation);
		final Map<String, Set<String>> roleMap = asMap(mapping);

		return roleMap;
	}

}
