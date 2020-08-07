package de.vsfexperts.rbac.spring.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class RbacPropertiesTest {

	private RbacProperties properties;

	@Before
	public void setUp() {
		properties = new RbacProperties();
	}

	@Test
	public void testConfigLocation() {
		final String value = "configLocation";
		properties.setConfigLocation(value);

		assertThat(properties.getConfigLocation(), is(value));
	}

	@Test
	public void testSpringRoles() {
		final boolean value = true;
		properties.setSpringRoles(value);

		assertThat(properties.isSpringRoles(), is(value));
	}

}
