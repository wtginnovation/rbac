package de.vsfexperts.rbac.configuration.domain.io;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import de.vsfexperts.rbac.configuration.domain.Privilege;
import de.vsfexperts.rbac.configuration.domain.Role;
import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import de.vsfexperts.rbac.configuration.io.YamlLoader;

public class YamlLoaderTest {

	private Role adminRole;
	private Privilege adminPrivilege1;
	private Privilege adminPrivilege2;
	private Privilege adminPrivilege3;
	private RoleMapping adminRoleMapping;

	private Role userRole;
	private Privilege userPrivilege1;
	private Privilege userPrivilege2;
	private RoleMapping userRoleMapping;

	@Before
	public void setUp() {
		createAdminRoleMapping();
		createUserRoleMapping();
	}

	private void createAdminRoleMapping() {
		adminRole = new Role("admin");

		adminPrivilege1 = new Privilege("list_orders");
		adminPrivilege2 = new Privilege("update_orders");
		adminPrivilege3 = new Privilege("delete_orders");

		final SortedSet<Privilege> privileges = new TreeSet<>();
		privileges.add(adminPrivilege1);
		privileges.add(adminPrivilege2);
		privileges.add(adminPrivilege3);

		adminRoleMapping = new RoleMapping();
		adminRoleMapping.setRole(adminRole);
		adminRoleMapping.setPrivileges(privileges);
	}

	private void createUserRoleMapping() {
		userRole = new Role("user");

		userPrivilege1 = new Privilege("list_orders");
		userPrivilege2 = new Privilege("update_orders");

		final SortedSet<Privilege> privileges = new TreeSet<>();
		privileges.add(userPrivilege1);
		privileges.add(userPrivilege2);

		userRoleMapping = new RoleMapping();
		userRoleMapping.setRole(userRole);
		userRoleMapping.setPrivileges(privileges);
	}

	@Test
	public void testLoadConfig() throws IOException {
		final Set<RoleMapping> mappings = new YamlLoader().load(new ClassPathResource("rbac.yaml"));

		assertThat(mappings.size(), is(2));
		assertThat(mappings, hasItem(userRoleMapping));
		assertThat(mappings, hasItem(adminRoleMapping));
	}

	@Test(expected = NullPointerException.class)
	public void testLoadConfigNull() throws IOException {
		new YamlLoader().load(null);
		fail();
	}

}
