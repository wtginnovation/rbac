package de.vsfexperts.rbac.spring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import de.vsfexperts.rbac.spring.configuration.RbacProperties;
import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.oauth2.RbacUserAuthenticationConverter;

/**
 * Central auto configuration in order to provide a custom
 * {@link UserAuthenticationConverter}
 */
@Configuration
@ConditionalOnClass({ OAuth2AccessToken.class })
@AutoConfigureAfter({ RbacMappingAutoConfiguration.class })
public class RbacOauth2AutoConfiguration {

	@Autowired
	private RbacProperties properties;

	@Bean
	@Primary
	@Autowired
	@ConditionalOnMissingBean
	public RbacUserAuthenticationConverter rbacUserAuthenticationConverter(final RoleMapper roleMapper)
			throws IOException {

		final RbacUserAuthenticationConverter converter = new RbacUserAuthenticationConverter(roleMapper);
		converter.setRoleClaimFieldname(properties.getRoleClaimFieldname());
		converter.setUserFieldname(properties.getUserFieldname());

		return converter;
	}

}
