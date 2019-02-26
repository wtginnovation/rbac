package de.vsfexperts.rbac.configuration.domain;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.vsfexperts.rbac.configuration.domain.Privilege;

public class PrivilegeTest {

	@Test(expected = NullPointerException.class)
	public void testNullConstructor() {
		new Privilege(null);
		fail();
	}

	@Test(expected = NullPointerException.class)
	public void testEmptyConstructor() {
		new Privilege("  ");
	}

	@Test
	public void testValidConstructor() {
		final Privilege privilege = new Privilege("  privilege  ");
		assertThat(privilege.getName(), is("PRIVILEGE"));
	}

	@Test
	public void testDefaultConstructor() {
		final Privilege privilege = new Privilege();
		assertThat(privilege, is(not(nullValue())));
	}

	@Test
	public void testEquals() {
		final Privilege privilege1 = new Privilege("privilege");
		final Privilege privilege2 = new Privilege("privilege");
		final Privilege privilege3 = new Privilege("privilege");

		assertThat(privilege1, is(privilege2));
		assertThat(privilege2, is(privilege3));
		assertThat(privilege2, is(privilege1));
		assertThat(privilege1, is(privilege3));
	}

	@Test
	public void testHashCode() {
		final int privilege1 = new Privilege("privilege").hashCode();
		final int privilege2 = new Privilege("privilege").hashCode();

		assertThat(privilege1, is(privilege2));
	}

	@Test
	public void testToString() {
		final String privilege1 = new Privilege("privilege1").toString();

		assertThat(privilege1, containsString("PRIVILEGE1"));
	}

	@Test
	public void testCompareTo() {
		final Privilege privilege1 = new Privilege("privilege");
		final Privilege privilege2 = new Privilege("privilege");
		final Privilege privilege3 = new Privilege("privilegeNotEqual");

		assertThat(privilege1.compareTo(privilege2), is(0));
		assertThat(privilege2.compareTo(privilege1), is(0));

		assertThat(privilege1.compareTo(privilege3) < 0, is(true));
		assertThat(privilege1.compareTo(null) < 0, is(true));
	}

}
