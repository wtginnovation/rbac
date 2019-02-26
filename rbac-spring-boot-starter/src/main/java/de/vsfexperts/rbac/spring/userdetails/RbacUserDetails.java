package de.vsfexperts.rbac.spring.userdetails;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Wrapper of {@link UserDetails}, which is overriding the backing privileges
 * with the privileges of this class. All other calls are redirected to the
 * backing {@link UserDetails} object.
 */
public class RbacUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final UserDetails delegate;
	private final Set<GrantedAuthority> privileges;

	public RbacUserDetails(final UserDetails delegate, final Set<GrantedAuthority> privileges) {
		requireNonNull(delegate, "userdetails must not be null");
		requireNonNull(privileges, "privileges must not be null");

		this.delegate = delegate;
		this.privileges = privileges;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableSet(privileges);
	}

	@Override
	public String getPassword() {
		return delegate.getPassword();
	}

	@Override
	public String getUsername() {
		return delegate.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return delegate.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return delegate.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return delegate.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	public UserDetails getDelegate() {
		return delegate;
	}

	@Override
	public String toString() {
		return "RbacUserDetails [username=" + getUsername() + ", authorities=" + getAuthorities() + "]";
	}

}
