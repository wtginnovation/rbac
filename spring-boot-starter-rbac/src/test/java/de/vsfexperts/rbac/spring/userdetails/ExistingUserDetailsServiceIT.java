package de.vsfexperts.rbac.spring.userdetails;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacMappingAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacUserDetailsServiceAutoConfiguration;
import de.vsfexperts.rbac.spring.userdetails.RbacUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ExistingUserDetailsServiceIT.OverrideConfiguration.class,
		RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class,
		RbacUserDetailsServiceAutoConfiguration.class })
@ActiveProfiles("test")
public class ExistingUserDetailsServiceIT {

	@Configuration
	public static class OverrideConfiguration {

		public static final InMemoryUserDetailsManager EXPECTED_USER_DETAILS_SERVICE = new InMemoryUserDetailsManager();

		@Bean
		public UserDetailsService existingUserDetailsService() {
			return EXPECTED_USER_DETAILS_SERVICE;
		}
	}

	@Autowired
	private RbacUserDetailsService userDetailService;

	@Test
	public void testAutomaticallyInjectExistingUserDetailsService() {
		assertThat(userDetailService.getUserDetailsService() == OverrideConfiguration.EXPECTED_USER_DETAILS_SERVICE,
				is(true));
	}
}
