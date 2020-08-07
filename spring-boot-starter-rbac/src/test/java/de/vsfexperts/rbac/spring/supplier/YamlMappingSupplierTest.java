package de.vsfexperts.rbac.spring.supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import de.vsfexperts.rbac.spring.supplier.YamlMappingSupplier;

public class YamlMappingSupplierTest {

	@Test(expected = NullPointerException.class)
	public void testNullConstructor() {
		new YamlMappingSupplier(null);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingConfigFile() {
		final ClassPathResource mappingLocation = new ClassPathResource("doesNotExist");
		new YamlMappingSupplier(mappingLocation).get();
		fail();
	}

	@Test
	public void testLoadConfigFile() {
		final ClassPathResource mappingLocation = new ClassPathResource("rbac-test.yaml");
		final Map<String, Set<String>> mapping = new YamlMappingSupplier(mappingLocation).get();

		assertThat(mapping.size(), is(3));
		assertThat(mapping.containsKey("ADMIN"), is(true));
		assertThat(mapping.containsKey("USER"), is(true));
		assertThat(mapping.containsKey("NONE"), is(true));

	}

}
