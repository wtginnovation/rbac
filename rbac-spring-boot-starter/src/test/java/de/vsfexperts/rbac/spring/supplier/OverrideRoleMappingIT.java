package de.vsfexperts.rbac.spring.supplier;

import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import de.vsfexperts.rbac.spring.RbacMappingAutoConfiguration;
import de.vsfexperts.rbac.spring.RbacPropertiesAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OverrideRoleMappingIT.OverrideConfiguration.class, RbacPropertiesAutoConfiguration.class,
		RbacMappingAutoConfiguration.class, })
@ActiveProfiles("test")
public class OverrideRoleMappingIT {

	@Configuration
	public static class OverrideConfiguration {

		@Bean
		public RbacMappingSupplier overriddenMappingLoader() {
			return () -> {
				final Map<String, Set<String>> result = new HashMap<>();
				result.put("OVERRIDDEN", emptySet());

				return result;
			};
		}
	}

	@Autowired
	private RbacMappingSupplier mappingSupplier;

	@Test
	public void testOverriddenRoleMapping() {
		final Map<String, Set<String>> roleMap = mappingSupplier.get();
		assertThat(roleMap.size(), is(1));
		assertThat(roleMap.containsKey("OVERRIDDEN"), is(true));
	}

}
