package de.vsfexperts.rbac.spring.userdetails;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.vsfexperts.rbac.spring.mapping.RoleMapper;

/**
 * Implementation of {@link UserDetailsService} in order to provide a dynamic
 * mapping of roles to privileges. Backing {@link UserDetailsService} must
 * provide <b>roles</b> in its {@link UserDetails} (as
 * {@link GrantedAuthority}), instead of privileges.
 */
public class RbacUserDetailsService implements UserDetailsService {

	private UserDetailsService userDetailsService;
	private final RoleMapper roleMapper;

	public RbacUserDetailsService(final RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) {

		final UserDetails user = userDetailsService.loadUserByUsername(username);

		final Set<String> roles = AuthorityUtils.authorityListToSet(user.getAuthorities());

		final Set<GrantedAuthority> privileges = roleMapper.mapRolesToGrantedAuthority(roles);

		return new RbacUserDetails(user, privileges);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailService(final UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public RoleMapper getRoleMapper() {
		return roleMapper;
	}

}
