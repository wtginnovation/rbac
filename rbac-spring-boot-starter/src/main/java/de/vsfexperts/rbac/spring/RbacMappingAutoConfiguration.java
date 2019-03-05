package de.vsfexperts.rbac.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import de.vsfexperts.rbac.spring.configuration.RbacProperties;
import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.supplier.CachingSupplier;
import de.vsfexperts.rbac.spring.supplier.LoggingSupplier;
import de.vsfexperts.rbac.spring.supplier.RbacMappingSupplier;
import de.vsfexperts.rbac.spring.supplier.YamlMappingSupplier;

/**
 * Central auto configuration in order provide the mapping from rbac.yaml file
 */
@Configuration
@AutoConfigureAfter(RbacPropertiesAutoConfiguration.class)
public class RbacMappingAutoConfiguration {

	@Autowired
	private RbacProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public RbacMappingSupplier rbacMappingSupplier() {
		final ClassPathResource configLocation = new ClassPathResource(properties.getConfigLocation());
		final YamlMappingSupplier mappingSupplier = new YamlMappingSupplier(configLocation);

		return new CachingSupplier(new LoggingSupplier(mappingSupplier));
	}

	@Bean
	@Autowired
	@ConditionalOnMissingBean
	public RoleMapper roleMapper(final RbacMappingSupplier supplier) {

		final RoleMapper mapper = new RoleMapper(supplier);
		mapper.setSpringRoles(properties.isSpringRoles());

		return mapper;
	}

}
