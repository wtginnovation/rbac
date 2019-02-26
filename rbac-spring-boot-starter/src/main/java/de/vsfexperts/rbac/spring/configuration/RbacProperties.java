package de.vsfexperts.rbac.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration options of this module in application config.
 */
@ConfigurationProperties(prefix = "rbac")
public class RbacProperties {

	private String configLocation = "rbac.yaml";

	private boolean springRoles = true;

	private String roleClaimFieldname = "authorities";

	private String userFieldname = "user_name";

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(final String configLocation) {
		this.configLocation = configLocation;
	}

	public boolean isSpringRoles() {
		return springRoles;
	}

	public void setSpringRoles(final boolean springRoles) {
		this.springRoles = springRoles;
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
