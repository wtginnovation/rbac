package de.vsfexperts.rbac.configuration.io;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;

/**
 * Populate mapping from YAML file.
 */
public class YamlLoader {

	public Set<RoleMapping> load(final Resource resource) throws IOException {
		requireNonNull(resource);

		final Set<RoleMapping> result = new HashSet<>();

		final Iterable<Object> mappings = initializeYaml().loadAll(resource.getInputStream());

		for (final Object mapping : mappings) {
			result.add((RoleMapping) mapping);
		}

		return result;
	}

	private Yaml initializeYaml() {
		return new Yaml(new Constructor(RoleMapping.class));
	}

}
