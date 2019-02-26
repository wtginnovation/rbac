package de.vsfexperts.rbac.spring.oauth2;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.oauth2.RbacUserAuthenticationConverter;

public class RbacUserAuthenticationConverterTest {

	private static final String AUTHORITIES = "my_authorities_field";
	private static final String USER = "my_user_field";

	private Map<String, Set<String>> mapping;
	private RoleMapper roleMapper;
	private RbacUserAuthenticationConverter converter;

	@Before
	public void setUp() {
		mapping = new HashMap<>();
		mapping.put("ROLE1", new HashSet<>(asList("PRIVILEGE_1")));
		mapping.put("ROLE2", new HashSet<>(asList("PRIVILEGE_1", "PRIVILEGE_2")));

		roleMapper = new RoleMapper(() -> mapping);
		roleMapper.setSpringRoles(true);

		converter = new RbacUserAuthenticationConverter(roleMapper);
		converter.setRoleClaimFieldname(AUTHORITIES);
		converter.setUserFieldname(USER);
	}

	@Test(expected = NullPointerException.class)
	public void testNullRoleMapper() {
		new RbacUserAuthenticationConverter(null);
		fail();
	}

	@Test
	public void testRoleClaimFieldnameField() {
		converter.setRoleClaimFieldname("roleClaimFieldname");
		assertThat(converter.getRoleClaimFieldname(), is("roleClaimFieldname"));
	}

	@Test
	public void testUsernameField() {
		converter.setUserFieldname("userFieldname");
		assertThat(converter.getUserFieldname(), is("userFieldname"));
	}

	@Test
	public void testNoRolesConversionNullAuthorities() {
		final Authentication authentication = new TestingAuthenticationToken("user", "N/A",
				(List<GrantedAuthority>) null);

		final Map<String, ?> result = converter.convertUserAuthentication(authentication);

		result.get(UserAuthenticationConverter.AUTHORITIES);
		final Object username = result.get(UserAuthenticationConverter.USERNAME);

		assertThat(result.size(), is(1));
		assertThat(username, is("user"));
	}

	@Test
	public void testNoRolesConversionEmptyAuthorities() {
		final Authentication authentication = new TestingAuthenticationToken("user", "N/A", emptyList());

		final Map<String, ?> result = converter.convertUserAuthentication(authentication);

		result.get(UserAuthenticationConverter.AUTHORITIES);
		final Object username = result.get(UserAuthenticationConverter.USERNAME);

		assertThat(result.size(), is(1));
		assertThat(username, is("user"));
	}

	@Test
	public void testDefaultConversionStringAuthorities() {
		final Authentication authentication = new TestingAuthenticationToken("user", "N/A", "PRIVILEGE_1");

		final Map<String, ?> result = converter.convertUserAuthentication(authentication);

		@SuppressWarnings("unchecked")
		final Collection<String> authorities = (Collection<String>) result.get(UserAuthenticationConverter.AUTHORITIES);
		final Object username = result.get(UserAuthenticationConverter.USERNAME);

		assertThat(result.size(), is(2));
		assertThat(username, is("user"));
		assertThat(authorities.size(), is(1));
		assertThat(authorities, hasItem("PRIVILEGE_1"));
	}

	@Test
	public void testDefaultConversionCollectionAuthorities() {
		final Map<String, Object> token = new HashMap<>();
		token.put(USER, "user");
		token.put(AUTHORITIES, asList("ROLE1"));

		final Authentication result = converter.extractAuthentication(token);

		@SuppressWarnings("unchecked")
		final Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) result.getAuthorities();

		assertThat(result.getName(), is("user"));
		assertThat(authorities.size(), is(1));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE_1")));
	}

	@Test
	public void testDefaultConversionStringSingleRole() {
		final Map<String, Object> token = new HashMap<>();
		token.put(USER, "user");
		token.put(AUTHORITIES, "ROLE1");

		final Authentication result = converter.extractAuthentication(token);

		@SuppressWarnings("unchecked")
		final Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) result.getAuthorities();

		assertThat(result.getName(), is("user"));
		assertThat(result.getAuthorities().size(), is(1));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE_1")));
	}

	@Test
	public void testDefaultConversionStringMultipleRoles() {
		final Map<String, Object> token = new HashMap<>();
		token.put(USER, "user");
		token.put(AUTHORITIES, "ROLE2");

		final Authentication result = converter.extractAuthentication(token);

		@SuppressWarnings("unchecked")
		final Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) result.getAuthorities();

		assertThat(result.getName(), is("user"));
		assertThat(result.getAuthorities().size(), is(2));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE_1")));
		assertThat(authorities, hasItem(new SimpleGrantedAuthority("ROLE_PRIVILEGE_2")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConversionInvalidAuthorities() {
		final Map<String, Object> token = new HashMap<>();
		token.put(USER, "user");
		token.put(AUTHORITIES, 5);

		converter.extractAuthentication(token);
		fail();
	}

	@Test
	public void testConversionMissingAuthoritiesField() {
		final Map<String, Object> token = new HashMap<>();
		token.put(USER, "user");

		final Authentication result = converter.extractAuthentication(token);

		assertThat(result.getName(), is("user"));
		assertThat(result.getAuthorities().isEmpty(), is(true));
	}

	@Test
	public void testConversionMissingUserField() {
		final Map<String, Object> token = new HashMap<>();

		final Authentication result = converter.extractAuthentication(token);

		assertThat(result, is(nullValue()));
	}

}
