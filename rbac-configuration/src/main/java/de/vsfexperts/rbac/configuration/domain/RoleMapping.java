package de.vsfexperts.rbac.configuration.domain;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;

import de.vsfexperts.rbac.configuration.domain.util.CollectionComparator;

/**
 * Mapping of a role to its corresponding set of privileges
 */
public class RoleMapping extends DomainObject implements Comparable<RoleMapping> {

	private Role role;
	private SortedSet<Privilege> privileges = new TreeSet<>();

	public RoleMapping() {
	}

	public Role getRole() {
		return role;
	}

	public String getRoleName() {
		return role.getName();
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	public SortedSet<Privilege> getPrivileges() {
		return Collections.unmodifiableSortedSet(privileges);
	}

	public SortedSet<String> getPrivilegeNames() {
		return privileges.stream().map(Privilege::getName).collect(Collectors.toCollection(TreeSet::new));
	}

	public void setPrivileges(final SortedSet<Privilege> privileges) {
		requireNonNull(privileges, "privileges must not be null");
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		return "RoleMapping [role=" + role + ", privileges=" + privileges + "]";
	}

	@Override
	public int compareTo(final RoleMapping other) {
		if (other == null) {
			return -1;
		}

		return new CompareToBuilder() //
				.append(role, other.getRole()) //
				.append(privileges, other.getPrivileges(), new CollectionComparator<>()) //
				.toComparison();
	}

}
