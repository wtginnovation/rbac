package de.vsfexperts.rbac.spring.mapping;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RoleMapperTest {

	private Map<String, Set<String>> roleMapping;
	private RoleMapper roleMapper;

	@Before
	public void setUp() {
		roleMapping = new HashMap<>();
		roleMapping.put("USER_WITH_PRIVILEGES_ROLE", new HashSet<>(asList("PRIVILEGE")));
		roleMapping.put("ROLE1", new HashSet<>(asList("PRIVILEGE1", "PRIVILEGE2")));
		roleMapping.put("ROLE2", new HashSet<>(asList("PRIVILEGE2", "PRIVILEGE3")));
		roleMapping.put("USER_WITHOUT_PRIVILEGES_ROLE", new HashSet<>());

		roleMapper = new RoleMapper(() -> roleMapping);
		roleMapper.setSpringRoles(true);
	}

	@Test
	public void testInitialization() {
		assertThat(roleMapper.isSpringRoles(), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testMissingUser() {
		roleMapper.mapRolesToGrantedAuthority(null);
		fail();
	}

	@Test
	public void testLoadUserWithPrivileges() {
		final Set<GrantedAuthority> authorities = roleMapper.mapRolesToGrantedAuthority(asList("USER_WITH_PRIVILEGES_ROLE"));

		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE")));
	}

	@Test
	public void testLoadUserWithPrivilegesAndNoSpringRoles() {
		roleMapper.setSpringRoles(false);

		final Set<GrantedAuthority> authorities = roleMapper.mapRolesToGrantedAuthority(asList("USER_WITH_PRIVILEGES_ROLE"));

		assertThat(authorities, hasItem(new SimpleGrantedAuthority("PRIVILEGE")));
	}

	@Test
	public void testLoadUserWithoutPrivileges() {
		final Set<GrantedAuthority> authorities = roleMapper
				.mapRolesToGrantedAuthority(asList("USER_WITHOUT_PRIVILEGES_ROLE"));

		assertThat(authorities.isEmpty(), is(true));
	}

	@Test
	public void testLoadUserWithoutMapping() {
		final Set<GrantedAuthority> authorities = roleMapper.mapRolesToGrantedAuthority(asList("USER_WITHOUT_MAPPING"));

		assertThat(authorities.isEmpty(), is(true));
	}

	@Test
	public void testLoadUserWithMultipleRoles() {
		final Set<GrantedAuthority> authorities = roleMapper.mapRolesToGrantedAuthority(asList("ROLE1", "ROLE2"));

		assertThat(authorities.size(), is(3));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE1")));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE2")));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE3")));
	}

}
