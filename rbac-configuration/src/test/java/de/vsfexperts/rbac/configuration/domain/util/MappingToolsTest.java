package de.vsfexperts.rbac.configuration.domain.util;

import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.asMap;
import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getPrivilegeNames;
import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getRoleNames;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import de.vsfexperts.rbac.configuration.domain.Privilege;
import de.vsfexperts.rbac.configuration.domain.Role;
import de.vsfexperts.rbac.configuration.domain.RoleMapping;

public class MappingToolsTest {

	private Role role;

	private Privilege privilege1;
	private Privilege privilege2;

	private RoleMapping mapping;

	private Set<RoleMapping> mappings;

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

		mappings = new HashSet<>(asList(mapping));
	}

	@Test
	public void testAsMapEmpty() {
		final Map<String, Set<String>> result = asMap(Collections.emptySet());

		assertThat(result.isEmpty(), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testAsMapNull() {
		asMap(null);
		fail();
	}

	@Test
	public void testAsMap() {
		final Map<String, Set<String>> result = asMap(mappings);

		final Set<String> privileges = result.get(role.getName());

		assertThat(result.size(), is(1));
		assertThat(privileges, is(not(nullValue())));
		assertThat(privileges, hasItem(privilege1.getName()));
		assertThat(privileges, hasItem(privilege2.getName()));
	}

	@Test
	public void testGetPrivilegeNamesEmpty() {
		final Set<String> result = getPrivilegeNames(Collections.emptySet());

		assertThat(result.isEmpty(), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testGetPrivilegeNamesNull() {
		getPrivilegeNames(null);
		fail();
	}

	@Test
	public void testGetPrivilegeNames() {
		final Set<String> result = getPrivilegeNames(mappings);

		assertThat(result.size(), is(2));
		assertThat(result, hasItem(privilege1.getName()));
		assertThat(result, hasItem(privilege2.getName()));
	}

	@Test
	public void testGetRoleNamesEmpty() {
		final Set<String> result = getRoleNames(Collections.emptySet());

		assertThat(result.isEmpty(), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testGetRoleNamesNull() {
		getRoleNames(null);
		fail();
	}

	@Test
	public void testGetRoleNames() {
		final Set<String> result = getRoleNames(mappings);

		assertThat(result.size(), is(1));
		assertThat(result, hasItem(role.getName()));
	}

}
