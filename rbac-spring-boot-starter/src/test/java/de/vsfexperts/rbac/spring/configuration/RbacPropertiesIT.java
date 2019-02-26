package de.vsfexperts.rbac.spring.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RbacProperties.class)
@EnableConfigurationProperties
@ActiveProfiles("properties-test")
public class RbacPropertiesIT {

	@Autowired
	private RbacProperties properties;

	@Test
	public void testConfiguredValues() {
		assertThat(properties.getConfigLocation(), is("rbac-test.yaml"));
		assertThat(properties.isSpringRoles(), is(false));
		assertThat(properties.getRoleClaimFieldname(), is("roleClaimFieldname"));
		assertThat(properties.getUserFieldname(), is("userFieldname"));
	}

}
