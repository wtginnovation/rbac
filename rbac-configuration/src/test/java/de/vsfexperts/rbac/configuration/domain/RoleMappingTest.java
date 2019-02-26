package de.vsfexperts.rbac.configuration.domain;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import de.vsfexperts.rbac.configuration.domain.Privilege;
import de.vsfexperts.rbac.configuration.domain.Role;
import de.vsfexperts.rbac.configuration.domain.RoleMapping;

public class RoleMappingTest {

	private Role role;

	private Privilege privilege1;
	private Privilege privilege2;

	private RoleMapping mapping;

	@Before
	public void setUp() {
		role = new Role("ROLE");

		privilege1 = new Privilege("PRIVILEGE1");
		privilege2 = new Privilege("PRIVILEGE2");

		final SortedSet<Privilege> privileges = new TreeSet<>();
		privileges.add(privilege1);
		privileges.add(privilege2);

		mapping = new RoleMapping();
		mapping.setRole(role);
		mapping.setPrivileges(privileges);
	}

	@Test
	public void testSetRole() {
		assertThat(mapping.getRole(), is(role));
	}

	@Test
	public void testGetRoleName() {
		assertThat(mapping.getRoleName(), is(role.getName()));
	}

	@Test
	public void testSetPrivileges() {
		final SortedSet<Privilege> privileges = new TreeSet<>();

		mapping.setPrivileges(privileges);

		assertThat(mapping.getPrivileges(), is(privileges));
	}

	@Test(expected = NullPointerException.class)
	public void testSetPrivilegesNull() {
		mapping.setPrivileges(null);
		fail();
	}

	@Test
	public void testGetPrivileges() {
		final Set<Privilege> privileges = mapping.getPrivileges();

		assertThat(privileges.size(), is(2));
		assertThat(privileges, hasItem(privilege1));
		assertThat(privileges, hasItem(privilege2));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetPrivilegesImmutable() {
		mapping.getPrivileges().add(privilege1);
		fail();
	}

	@Test
	public void testGetPrivilegesNames() {
		final Set<String> privilegeNames = mapping.getPrivilegeNames();

		assertThat(privilegeNames.size(), is(2));
		assertThat(privilegeNames, hasItem(privilege1.getName()));
		assertThat(privilegeNames, hasItem(privilege2.getName()));
	}

	@Test
	public void testGetPrivilegesNamesEmpty() {
		mapping.setPrivileges(new TreeSet<>());

		assertThat(mapping.getPrivilegeNames().isEmpty(), is(true));
	}

	@Test
	public void testEquals() {
		final RoleMapping mapping1 = new RoleMapping();
		mapping1.setRole(new Role("ROLE"));
		mapping1.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping2 = new RoleMapping();
		mapping2.setRole(new Role("ROLE"));
		mapping2.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping3 = new RoleMapping();
		mapping3.setRole(new Role("ROLE"));
		mapping3.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		assertThat(mapping1, is(mapping2));
		assertThat(mapping2, is(mapping3));
		assertThat(mapping2, is(mapping1));
		assertThat(mapping1, is(mapping3));
	}

	@Test
	public void testHashcode() {
		final RoleMapping mapping1 = new RoleMapping();
		mapping1.setRole(new Role("ROLE"));
		mapping1.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping2 = new RoleMapping();
		mapping2.setRole(new Role("ROLE"));
		mapping2.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		assertThat(mapping1.hashCode(), is(mapping2.hashCode()));
	}

	@Test
	public void testToString() {
		final String result = mapping.toString();

		assertThat(result, containsString(role.getName()));
		assertThat(result, containsString(privilege1.getName()));
		assertThat(result, containsString(privilege2.getName()));
	}

	@Test
	public void testCompareToNull() {
		final RoleMapping mapping1 = new RoleMapping();

		assertThat(mapping1.compareTo(null) < 0, is(true));
	}

	@Test
	public void testCompareToSame() {
		final RoleMapping mapping1 = new RoleMapping();
		mapping1.setRole(new Role("ROLE"));
		mapping1.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping2 = new RoleMapping();
		mapping2.setRole(new Role("ROLE"));
		mapping2.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		assertThat(mapping1.compareTo(mapping2), is(0));
	}

	@Test
	public void testCompareToDifferentRole() {
		final RoleMapping mapping1 = new RoleMapping();
		mapping1.setRole(new Role("ROLE"));
		mapping1.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping2 = new RoleMapping();
		mapping2.setRole(new Role("ROLE2"));
		mapping2.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		assertThat(mapping1.compareTo(mapping2) < 0, is(true));
	}

	@Test
	public void testCompareToDifferentPrivileges() {
		final RoleMapping mapping1 = new RoleMapping();
		mapping1.setRole(new Role("ROLE"));
		mapping1.setPrivileges(new TreeSet<>(asList(new Privilege("P1"), new Privilege("P2"))));

		final RoleMapping mapping2 = new RoleMapping();
		mapping2.setRole(new Role("ROLE"));
		mapping2.setPrivileges(new TreeSet<>(asList(new Privilege("P1"))));

		assertThat(mapping1.compareTo(mapping2) < 0, is(true));
	}

}
