package de.vsfexperts.rbac.spring.oauth2;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import de.vsfexperts.rbac.spring.mapping.RoleMapper;

/**
 * Simple {@link UserAuthenticationConverter} in order to override
 * authorities/roles from access token with configured privileges.
 */
public class RbacUserAuthenticationConverter implements UserAuthenticationConverter {

	private final RoleMapper roleMapper;
	private String userFieldname = USERNAME;
	private String roleClaimFieldname = AUTHORITIES;

	public RbacUserAuthenticationConverter(final RoleMapper roleMapper) {
		requireNonNull(roleMapper);
		this.roleMapper = roleMapper;
	}

	@Override
	public Map<String, ?> convertUserAuthentication(final Authentication authentication) {
		final Map<String, Object> response = new LinkedHashMap<>();
		response.put(USERNAME, authentication.getName());
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		return response;
	}

	@Override
	public Authentication extractAuthentication(final Map<String, ?> map) {
		final Object principal = map.get(userFieldname);

		if (principal == null) {
			return null;
		}

		final Collection<? extends GrantedAuthority> privileges = getPrivileges(map);
		return new UsernamePasswordAuthenticationToken(principal, "N/A", privileges);
	}

	@SuppressWarnings("unchecked")
	private Collection<? extends GrantedAuthority> getPrivileges(final Map<String, ?> map) {
		if (!map.containsKey(roleClaimFieldname)) {
			return Collections.emptySet();
		}
		final Object authorities = map.get(roleClaimFieldname);
		if (authorities instanceof String) {
			final String[] roles = StringUtils.tokenizeToStringArray((String) authorities, ",");
			return roleMapper.mapRolesToGrantedAuthority(Arrays.asList(roles));
		}
		if (authorities instanceof Collection) {
			return roleMapper.mapRolesToGrantedAuthority((Collection<String>) authorities);
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}

	public String getRoleClaimFieldname() {
		return roleClaimFieldname;
	}

	public void setRoleClaimFieldname(final String roleClaimFieldname) {
		this.roleClaimFieldname = roleClaimFieldname;
	}

	public String getUserFieldname() {
		return userFieldname;
	}

	public void setUserFieldname(final String userFieldname) {
		this.userFieldname = userFieldname;
	}

}