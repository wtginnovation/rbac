package de.vsfexperts.rbac.spring.mapping;

import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

/**
 * Simple {@link GrantedAuthoritiesMapper} in order to convert defined roles to
 * privileges.
 */
public class RbacAuthoritiesMapper implements GrantedAuthoritiesMapper {

	private final RoleMapper roleMapper;

	public RbacAuthoritiesMapper(final RoleMapper roleMapper) {
		requireNonNull(roleMapper);
		this.roleMapper = roleMapper;
	}

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(
			final Collection<? extends GrantedAuthority> authorities) {

		if (authorities == null || authorities.isEmpty()) {
			return emptySet();
		}

		final Set<String> roles = AuthorityUtils.authorityListToSet(authorities);

		return roleMapper.mapRolesToGrantedAuthority(roles);
	}

}