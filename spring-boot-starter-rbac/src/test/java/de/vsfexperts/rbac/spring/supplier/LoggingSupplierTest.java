package de.vsfexperts.rbac.spring.supplier;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LoggingSupplierTest {

	@Test(expected = NullPointerException.class)
	public void testNullConstructor() {
		new LoggingSupplier(null);
		fail();
	}

	@Test
	public void testDefaultBehaviourEmptyResult() {
		final Map<String, Set<String>> expectedResult = new HashMap<>();

		final RbacMappingSupplier supplier = () -> expectedResult;

		final Map<String, Set<String>> result = new LoggingSupplier(supplier).get();

		assertThat(result, is(expectedResult));
	}

	@Test
	public void testDefaultBehaviourNullResult() {
		final RbacMappingSupplier supplier = () -> null;

		final Map<String, Set<String>> result = new LoggingSupplier(supplier).get();

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testDefaultBehaviour() {
		final Map<String, Set<String>> expectedResult = new HashMap<>();
		expectedResult.put("key", new HashSet<>(asList("value1", "value2")));

		final RbacMappingSupplier supplier = () -> expectedResult;

		final Map<String, Set<String>> result = new LoggingSupplier(supplier).get();

		assertThat(result, is(expectedResult));
	}

}
