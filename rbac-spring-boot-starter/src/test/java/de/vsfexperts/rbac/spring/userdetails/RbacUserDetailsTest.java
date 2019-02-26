package de.vsfexperts.rbac.spring.userdetails;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import de.vsfexperts.rbac.spring.userdetails.RbacUserDetails;

public class RbacUserDetailsTest {

	private static final String USERNAME = "username";
	private static final String ROLE = "role";
	private static final String PRIVILEGE = "privilege";

	private Set<GrantedAuthority> roles;
	private Set<GrantedAuthority> privileges;
	private UserDetails user;

	private RbacUserDetails userDetails;

	@Before
	public void setUp() {
		roles = new HashSet<>();
		roles.add(new SimpleGrantedAuthority(ROLE));

		user = new User(USERNAME, "password", roles);

		privileges = new HashSet<>();
		privileges.add(new SimpleGrantedAuthority(PRIVILEGE));

		userDetails = new RbacUserDetails(user, privileges);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorMissingUserDetails() {
		new RbacUserDetails(null, privileges);
		fail();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorMissingPrivileges() {
		new RbacUserDetails(user, null);
		fail();
	}

	@Test
	public void testGetAuthorities() {
		assertThat(userDetails.getAuthorities(), is(privileges));
	}

	@Test
	public void testGetDelegate() {
		assertThat(userDetails.getDelegate(), is(user));
	}

	@Test
	public void testGetPassword() {
		assertThat(userDetails.getPassword(), is(user.getPassword()));
	}

	@Test
	public void testGetUsername() {
		assertThat(userDetails.getUsername(), is(user.getUsername()));
	}

	@Test
	public void testIsAccountNonExpired() {
		assertThat(userDetails.isAccountNonExpired(), is(user.isAccountNonExpired()));
	}

	@Test
	public void testIsAccountNonLocked() {
		assertThat(userDetails.isAccountNonLocked(), is(user.isAccountNonLocked()));
	}

	@Test
	public void testIsCredentialsNonExpired() {
		assertThat(userDetails.isCredentialsNonExpired(), is(user.isCredentialsNonExpired()));
	}

	@Test
	public void testIsEnabled() {
		assertThat(userDetails.isEnabled(), is(user.isEnabled()));
	}

	@Test
	public void testToString() {
		final String string = userDetails.toString();

		assertThat(string, containsString(USERNAME));
		assertThat(string, containsString(PRIVILEGE));
	}

}
