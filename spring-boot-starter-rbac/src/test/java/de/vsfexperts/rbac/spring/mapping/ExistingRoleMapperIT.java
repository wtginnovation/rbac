package de.vsfexperts.rbac.spring.mapping;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacMappingAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ExistingRoleMapperIT.OverrideConfiguration.class, RbacMappingAutoConfiguration.class,
		RbacPropertiesAutoConfiguration.class })
@ActiveProfiles("test")
public class ExistingRoleMapperIT {

	@Configuration
	public static class OverrideConfiguration {

		public static final RoleMapper EXPECTED_ROLE_MAPPER = new RoleMapper(() -> null);
		public static final RbacAuthoritiesMapper EXPECTED_RBAC_MAPPER = new RbacAuthoritiesMapper(
				EXPECTED_ROLE_MAPPER);

		@Bean
		public RoleMapper existingRbacUserAuthenticationConverter() {
			return EXPECTED_ROLE_MAPPER;
		}

		@Bean
		public RbacAuthoritiesMapper existingRbacAuthoritiesMapper() {
			return EXPECTED_RBAC_MAPPER;
		}
	}

	@Autowired
	private RoleMapper mapper;

	@Autowired
	private RbacAuthoritiesMapper rbacAuthoritiesMapper;

	@Test
	public void testAutomaticallyInjectMapper() {
		assertThat(mapper == OverrideConfiguration.EXPECTED_ROLE_MAPPER, is(true));
	}

	@Test
	public void testAutomaticallyInjectRbacAuthoritiesMapper() {
		assertThat(rbacAuthoritiesMapper == OverrideConfiguration.EXPECTED_RBAC_MAPPER, is(true));
	}
}
