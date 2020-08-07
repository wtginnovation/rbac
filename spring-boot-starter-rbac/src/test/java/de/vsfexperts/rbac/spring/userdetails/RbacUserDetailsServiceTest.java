package de.vsfexperts.rbac.spring.userdetails;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import de.vsfexperts.rbac.spring.mapping.RoleMapper;

public class RbacUserDetailsServiceTest {

	private UserDetails userWithPrivileges;
	private UserDetails userWithoutPrivileges;
	private UserDetails userWithoutMapping;

	private InMemoryUserDetailsManager userDetailsManager;

	private RbacUserDetailsService userDetailsService;
	private Map<String, Set<String>> roleMapping;
	private UserDetails userWithMultipleRoles;
	private RoleMapper roleMapper;

	@Before
	public void setUp() {
		userWithPrivileges = User //
				.withUsername("user") //
				.password("{noop}password") //
				.roles("user_With_Privileges_role") //
				.build();

		userWithoutPrivileges = User //
				.withUsername("userWithoutPrivileges") //
				.password("{noop}password") //
				.roles("user_Without_Privileges_role") //
				.build();

		userWithoutMapping = User //
				.withUsername("userWithoutMapping") //
				.password("{noop}password") //
				.roles("user_Without_Mapping_role") //
				.build();

		userWithMultipleRoles = User //
				.withUsername("userWithMultipleRoles") //
				.password("{noop}password") //
				.roles("role1", "unmappedRole1", "role2", "unmappedRole2") //
				.build();

		userDetailsManager = new InMemoryUserDetailsManager();
		userDetailsManager.createUser(userWithPrivileges);
		userDetailsManager.createUser(userWithoutPrivileges);
		userDetailsManager.createUser(userWithoutMapping);
		userDetailsManager.createUser(userWithMultipleRoles);

		roleMapping = new HashMap<>();
		roleMapping.put("USER_WITH_PRIVILEGES_ROLE", new HashSet<>(asList("PRIVILEGE")));
		roleMapping.put("ROLE1", new HashSet<>(asList("PRIVILEGE1", "PRIVILEGE2")));
		roleMapping.put("ROLE2", new HashSet<>(asList("PRIVILEGE2", "PRIVILEGE3")));
		roleMapping.put("USER_WITHOUT_PRIVILEGES_ROLE", new HashSet<>());

		roleMapper = new RoleMapper(() -> roleMapping);
		roleMapper.setSpringRoles(true);

		userDetailsService = new RbacUserDetailsService(roleMapper);
		userDetailsService.setUserDetailService(userDetailsManager);
	}

	@Test
	public void testInitialization() {
		assertThat(userDetailsService.getRoleMapper(), is(roleMapper));
		assertThat(userDetailsService.getUserDetailsService(), is(userDetailsManager));
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testMissingUser() {
		userDetailsService.loadUserByUsername("userDoesNotExist");
		fail();
	}

	@Test
	public void testLoadUserWithPrivileges() {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userWithPrivileges.getUsername());

		verifyCommonFields(userDetails, userWithPrivileges);

		assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE")), is(true));
	}

	@Test
	public void testLoadUserWithPrivilegesAndNoSpringRoles() {
		roleMapper.setSpringRoles(false);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(userWithPrivileges.getUsername());

		verifyCommonFields(userDetails, userWithPrivileges);

		assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PRIVILEGE")), is(true));
	}

	@Test
	public void testLoadUserWithoutPrivileges() {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userWithoutPrivileges.getUsername());

		verifyCommonFields(userDetails, userWithoutPrivileges);

		assertThat(userDetails.getAuthorities().isEmpty(), is(true));
	}

	@Test
	public void testLoadUserWithoutMapping() {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userWithoutMapping.getUsername());

		verifyCommonFields(userDetails, userWithoutMapping);

		assertThat(userDetails.getAuthorities().isEmpty(), is(true));
	}

	@Test
	public void testLoadUserWithMultipleRoles() {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userWithMultipleRoles.getUsername());

		verifyCommonFields(userDetails, userWithMultipleRoles);

		assertThat(userDetails.getAuthorities().size(), is(3));
		assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE1")), is(true));
		assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE2")), is(true));
		assertThat(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRIVILEGE3")), is(true));
	}

	private void verifyCommonFields(final UserDetails userDetails, final UserDetails reference) {
		assertThat(userDetails.getPassword(), is(reference.getPassword()));
		assertThat(userDetails.getUsername(), is(reference.getUsername()));
		assertThat(userDetails.isAccountNonExpired(), is(reference.isAccountNonExpired()));
		assertThat(userDetails.isAccountNonLocked(), is(reference.isAccountNonLocked()));
		assertThat(userDetails.isCredentialsNonExpired(), is(reference.isCredentialsNonExpired()));
		assertThat(userDetails.isEnabled(), is(reference.isEnabled()));
	}

}
