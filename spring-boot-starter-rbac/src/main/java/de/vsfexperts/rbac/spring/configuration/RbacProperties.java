package de.vsfexperts.rbac.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration options of this module in application config.
 */
@ConfigurationProperties(prefix = "rbac")
public class RbacProperties {

	private String configLocation = "rbac.yaml";

	private boolean springRoles = true;

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

}
