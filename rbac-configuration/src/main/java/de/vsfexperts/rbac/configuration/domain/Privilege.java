package de.vsfexperts.rbac.configuration.domain;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Simple representation of a privilege
 */
public class Privilege extends DomainObject implements Comparable<Privilege> {

	private String name;

	public Privilege() {
	}

	public Privilege(final String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		final String realName = trimToNull(name).toUpperCase();
		requireNonNull(realName, "name must not be null or empty");
		this.name = realName;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(final Privilege other) {
		if (other == null) {
			return -1;
		}
		return new CompareToBuilder().append(name, other.getName()).toComparison();
	}

}
