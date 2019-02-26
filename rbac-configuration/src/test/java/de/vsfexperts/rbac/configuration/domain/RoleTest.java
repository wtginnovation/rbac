package de.vsfexperts.rbac.configuration.domain;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.vsfexperts.rbac.configuration.domain.Role;

public class RoleTest {

	@Test(expected = NullPointerException.class)
	public void testNullConstructor() {
		new Role(null);
		fail();
	}

	@Test(expected = NullPointerException.class)
	public void testEmptyConstructor() {
		new Role("  ");
	}

	@Test
	public void testValidConstructor() {
		final Role role = new Role("  role  ");
		assertThat(role.getName(), is("ROLE"));
	}

	@Test
	public void testDefaultConstructor() {
		final Role role = new Role();
		assertThat(role, is(not(nullValue())));
	}

	@Test
	public void testEquals() {
		final Role role1 = new Role("role");
		final Role role2 = new Role("role");
		final Role role3 = new Role("role");

		assertThat(role1, is(role2));
		assertThat(role2, is(role3));
		assertThat(role2, is(role1));
		assertThat(role1, is(role3));
	}

	@Test
	public void testHashCode() {
		final int role1 = new Role("role").hashCode();
		final int role2 = new Role("role").hashCode();

		assertThat(role1, is(role2));
	}

	@Test
	public void testToString() {
		final String role1 = new Role("role1").toString();

		assertThat(role1, containsString("ROLE1"));
	}

	@Test
	public void testCompareTo() {
		final Role role1 = new Role("role");
		final Role role2 = new Role("role");
		final Role role3 = new Role("roleNotEqual");

		assertThat(role1.compareTo(role2), is(0));
		assertThat(role2.compareTo(role1), is(0));

		assertThat(role1.compareTo(role3) < 0, is(true));
		assertThat(role1.compareTo(null) < 0, is(true));
	}

}
