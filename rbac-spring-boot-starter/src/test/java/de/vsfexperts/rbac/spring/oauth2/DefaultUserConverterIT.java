package de.vsfexperts.rbac.spring.oauth2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacMappingAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacOauth2AutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;
import de.vsfexperts.rbac.spring.oauth2.RbacUserAuthenticationConverter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RbacMappingAutoConfiguration.class, RbacPropertiesAutoConfiguration.class,
		RbacOauth2AutoConfiguration.class })
@ActiveProfiles("test")
public class DefaultUserConverterIT {

	@Autowired
	private RbacUserAuthenticationConverter converter;

	@Test
	public void testConverter() {
		assertThat(converter.getUserFieldname(), is(UserAuthenticationConverter.USERNAME));
		assertThat(converter.getRoleClaimFieldname(), is(UserAuthenticationConverter.AUTHORITIES));
	}
}
