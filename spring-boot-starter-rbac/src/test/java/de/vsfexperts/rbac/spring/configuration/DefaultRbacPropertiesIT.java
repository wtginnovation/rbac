package de.vsfexperts.rbac.spring.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = RbacProperties.class)
public class DefaultRbacPropertiesIT {

	@Autowired
	private RbacProperties properties;

	@Test
	public void testDefaultValues() {
		assertThat(properties.getConfigLocation(), is("rbac.yaml"));
		assertThat(properties.isSpringRoles(), is(true));
	}

}
