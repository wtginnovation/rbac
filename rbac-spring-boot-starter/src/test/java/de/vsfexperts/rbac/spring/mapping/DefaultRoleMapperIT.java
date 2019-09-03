package de.vsfexperts.rbac.spring.mapping;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacMappingAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class })
@ActiveProfiles("test")
public class DefaultRoleMapperIT {

	@Autowired
	private RoleMapper mapper;

	@Autowired
	private RbacAuthoritiesMapper rbacAuthoritiesMapper;

	@Test
	public void testDefaultValues() {
		assertThat(mapper.isSpringRoles(), is(true));
	}

	@Test
	public void testRbacAuthoritiesMapper() {
		assertThat(rbacAuthoritiesMapper, is(not(nullValue())));
	}

}
