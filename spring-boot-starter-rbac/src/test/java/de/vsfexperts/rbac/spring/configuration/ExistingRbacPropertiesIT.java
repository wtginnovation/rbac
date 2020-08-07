package de.vsfexperts.rbac.spring.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ExistingRbacPropertiesIT.OverrideConfiguration.class,
		RbacPropertiesAutoConfiguration.class })
public class ExistingRbacPropertiesIT {

	@Configuration
	public static class OverrideConfiguration {

		@Bean
		public RbacProperties rbacProperties() {
			final RbacProperties properties = new RbacProperties();
			properties.setConfigLocation("rbac-test.yaml");

			return properties;
		}
	}

	@Autowired
	private RbacProperties properties;

	@Test
	public void testOverriddenProperties() {
		assertThat(properties.getConfigLocation(), is("rbac-test.yaml"));
	}

}
