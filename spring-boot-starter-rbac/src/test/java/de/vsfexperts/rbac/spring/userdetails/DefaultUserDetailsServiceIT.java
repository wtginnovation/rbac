package de.vsfexperts.rbac.spring.userdetails;

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
import de.vsfexperts.rbac.spring.RbacUserDetailsServiceAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class,
		RbacUserDetailsServiceAutoConfiguration.class })
@ActiveProfiles("test")
public class DefaultUserDetailsServiceIT {

	@Autowired
	private RbacUserDetailsService userDetailService;

	@Test
	public void testAutomaticallyInjectExistingUserDetailsService() {
		assertThat(userDetailService.getRoleMapper(), is(not(nullValue())));
		assertThat(userDetailService.getUserDetailsService(), is(nullValue()));
	}
}
