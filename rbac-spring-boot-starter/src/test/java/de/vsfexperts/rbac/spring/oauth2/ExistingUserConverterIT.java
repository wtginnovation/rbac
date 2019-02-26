package de.vsfexperts.rbac.spring.oauth2;

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
import de.vsfexperts.rbac.spring.RbacOauth2AutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;
import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.oauth2.RbacUserAuthenticationConverter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ExistingUserConverterIT.OverrideConfiguration.class,
		RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class, RbacOauth2AutoConfiguration.class })
@ActiveProfiles("test")
public class ExistingUserConverterIT {

	@Configuration
	public static class OverrideConfiguration {

		private static final RoleMapper ROLE_MAPPER = new RoleMapper(() -> null);

		public static final RbacUserAuthenticationConverter EXPECTED_CONVERTER = new RbacUserAuthenticationConverter(
				ROLE_MAPPER);

		@Bean
		public RbacUserAuthenticationConverter existingRbacUserAuthenticationConverter() {
			return EXPECTED_CONVERTER;
		}
	}

	@Autowired
	private RbacUserAuthenticationConverter converter;

	@Test
	public void testAutomaticallyInjectConverter() {
		assertThat(converter == OverrideConfiguration.EXPECTED_CONVERTER, is(true));
	}
}
