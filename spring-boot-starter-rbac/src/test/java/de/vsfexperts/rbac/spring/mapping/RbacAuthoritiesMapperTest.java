package de.vsfexperts.rbac.spring.mapping;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import de.vsfexperts.rbac.spring.mapping.RbacAuthoritiesMapper;
import de.vsfexperts.rbac.spring.mapping.RoleMapper;

public class RbacAuthoritiesMapperTest {

	private Map<String, Set<String>> mapping;
	private RoleMapper roleMapper;
	private RbacAuthoritiesMapper converter;

	@Before
	public void setUp() {
		mapping = new HashMap<>();
		mapping.put("ROLE1", new HashSet<>(asList("PRIVILEGE_1")));
		mapping.put("ROLE2", new HashSet<>(asList("PRIVILEGE_1", "PRIVILEGE_2")));

		roleMapper = new RoleMapper(() -> mapping);
		roleMapper.setSpringRoles(true);

		converter = new RbacAuthoritiesMapper(roleMapper);
	}

	@Test(expected = NullPointerException.class)
	public void testNullRoleMapper() {
		new RbacAuthoritiesMapper(null);
		fail();
	}

	@Test
	public void testNoRolesConversionNullAuthorities() {
		final Collection<? extends GrantedAuthority> result = converter.mapAuthorities(null);

		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void testNoRolesConversionEmptyAuthorities() {
		final Collection<? extends GrantedAuthority> result = converter.mapAuthorities(emptyList());

		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void testConversionNoMappedRoles() {
		final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("JUST_ANOTHER_ROLE");

		final Collection<? extends GrantedAuthority> result = converter.mapAuthorities(authorities);

		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void testConversionSingleRole() {
		final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE1");

		final Collection<? extends GrantedAuthority> result = converter.mapAuthorities(authorities);

		assertThat(result.size(), is(1));
		assertThat(result.contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE_1")), is(true));
	}

	@Test
	public void testConversionMultipleRoles() {
		final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ANOTHER_ROLE", "ROLE1",
				"JUST_ANOTHER_ROLE", "ROLE2", "AND_ANOTHER_ROLE");

		final Collection<? extends GrantedAuthority> result = converter.mapAuthorities(authorities);

		assertThat(result.size(), is(2));
		assertThat(result.contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE_1")), is(true));
		assertThat(result.contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE_2")), is(true));
	}

}
