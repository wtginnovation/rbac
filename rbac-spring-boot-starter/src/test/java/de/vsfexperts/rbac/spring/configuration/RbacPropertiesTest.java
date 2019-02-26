package de.vsfexperts.rbac.spring.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

	@Test
	public void testRoleClaimFieldname() {
		final String value = "roleClaimFieldname";
		properties.setRoleClaimFieldname(value);

		assertThat(properties.getRoleClaimFieldname(), is(value));
	}

	@Test
	public void testUserFieldname() {
		final String value = "userFieldname";
		properties.setUserFieldname(value);

		assertThat(properties.getUserFieldname(), is(value));
	}

}
