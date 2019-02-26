package de.vsfexperts.rbac.spring.mapping;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
@SpringBootTest(classes = { ExistingRoleMapperAutoConfigurationIT.OverrideConfiguration.class,
		RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class })
@ActiveProfiles("test")
public class ExistingRoleMapperAutoConfigurationIT {

	@Configuration
	public static class OverrideConfiguration {

		public static final RoleMapper EXPECTED_MAPPER = new RoleMapper(() -> null);

		@Bean
		public RoleMapper existingRbacUserAuthenticationConverter() {
			return EXPECTED_MAPPER;
		}
	}

	@Autowired
	private RoleMapper mapper;

	@Test
	public void testAutomaticallyInjectMapper() {
		assertThat(mapper == OverrideConfiguration.EXPECTED_MAPPER, is(true));
	}
}
