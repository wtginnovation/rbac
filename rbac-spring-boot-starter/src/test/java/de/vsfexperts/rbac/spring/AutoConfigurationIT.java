package de.vsfexperts.rbac.spring;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.configuration.RbacProperties;
import de.vsfexperts.rbac.spring.mapping.RoleMapper;
import de.vsfexperts.rbac.spring.supplier.RbacMappingSupplier;
import de.vsfexperts.rbac.spring.userdetails.RbacUserDetailsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RbacUserDetailsServiceAutoConfiguration.class, RbacMappingAutoConfiguration.class,
		RbacPropertiesAutoConfiguration.class })
@ActiveProfiles("test")
public class AutoConfigurationIT {

	@Autowired
	private RbacProperties properties;

	@Autowired
	private RbacUserDetailsService userDetailService;

	@Autowired
	private RbacMappingSupplier mappingSupplier;

	@Autowired
	private RoleMapper roleMapper;

	@Test
	public void testConfiguredProperties() {
		assertThat(properties.getConfigLocation(), is("rbac-test.yaml"));
		assertThat(properties.isSpringRoles(), is(true));
	}

	@Test
	public void testUserDetailService() {
		assertThat(userDetailService.getUserDetailsService(), is(nullValue()));
	}

	@Test
	public void testRoleMapper() {
		assertThat(roleMapper.isSpringRoles(), is(true));
	}

	@Test
	public void testRoleMapping() {
		final Map<String, Set<String>> roleMap = mappingSupplier.get();
		final Set<String> adminPrivileges = roleMap.get("ADMIN");
		final Set<String> userPrivileges = roleMap.get("USER");
		final Set<String> nonePrivileges = roleMap.get("NONE");

		assertThat(roleMap.size(), is(3));
		assertThat(adminPrivileges, hasItem("LIST_ORDERS"));
		assertThat(adminPrivileges, hasItem("UPDATE_ORDERS"));
		assertThat(adminPrivileges, hasItem("DELETE_ORDERS"));

		assertThat(userPrivileges, hasItem("LIST_ORDERS"));
		assertThat(userPrivileges, hasItem("UPDATE_ORDERS"));

		assertThat(nonePrivileges.isEmpty(), is(true));
	}

}
