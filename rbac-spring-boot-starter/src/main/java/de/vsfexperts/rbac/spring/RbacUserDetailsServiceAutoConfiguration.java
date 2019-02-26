package de.vsfexperts.rbac.spring;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.userdetails.RbacUserDetailsService;

/**
 * Central auto configuration in order to provide a custom
 * {@link UserDetailsService} ({@link RbacUserDetailsService})
 */
@Configuration
@ConditionalOnClass({ UserDetails.class, UserDetailsService.class })
@AutoConfigureAfter({ UserDetailsServiceAutoConfiguration.class, RbacMappingAutoConfiguration.class })
public class RbacUserDetailsServiceAutoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(RbacUserDetailsServiceAutoConfiguration.class);

	@Bean("rbacUserDetailsService")
	@Primary
	@Autowired
	@ConditionalOnBean(UserDetailsService.class)
	public RbacUserDetailsService fullyInitializedRbacUserDetailService(final UserDetailsService userDetailsService,
			final RoleMapper roleMapper) throws IOException {

		final RbacUserDetailsService rbacUserDetailService = rbacUserDetailsService(roleMapper);
		rbacUserDetailService.setUserDetailService(userDetailsService);

		return rbacUserDetailService;
	}

	@Bean("rbacUserDetailsService")
	@Primary
	@ConditionalOnMissingBean(UserDetailsService.class)
	public RbacUserDetailsService partiallyInitializedRbacUserDetailService(final RoleMapper roleMapper)
			throws IOException {

		LOG.warn("Partially configured RbacUserDetailsService. You'll have to inject a UserDetailsService later on.");
		return rbacUserDetailsService(roleMapper);
	}

	private RbacUserDetailsService rbacUserDetailsService(final RoleMapper roleMapper) {
		return new RbacUserDetailsService(roleMapper);
	}

}
