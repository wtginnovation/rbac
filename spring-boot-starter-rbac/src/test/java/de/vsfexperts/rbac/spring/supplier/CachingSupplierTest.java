package de.vsfexperts.rbac.spring.supplier;

import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class CachingSupplierTest {

	@Test(expected = NullPointerException.class)
	public void testNullConstructor() {
		new CachingSupplier(null);
		fail();
	}

	@Test
	public void testDefaultBehaviour() {
		final HashMap<String, Set<String>> expectedResult = new HashMap<>();
		expectedResult.put("success", emptySet());

		final RbacMappingSupplier cachedSupplier = () -> expectedResult;

		final CachingSupplier supplier = new CachingSupplier(cachedSupplier);

		final Map<String, Set<String>> result = supplier.get();

		assertThat(result.size(), is(1));
		assertThat(result.containsKey("success"), is(true));
		assertThat(result == expectedResult, is(true));
	}

	@Test
	public void testDefaultBehaviourEmptyResult() {
		final HashMap<String, Set<String>> expectedResult = new HashMap<>();

		final RbacMappingSupplier cachedSupplier = () -> expectedResult;

		final CachingSupplier supplier = new CachingSupplier(cachedSupplier);

		final Map<String, Set<String>> result = supplier.get();

		assertThat(result.isEmpty(), is(true));
		assertThat(result == expectedResult, is(true));
	}

	@Test
	public void testDefaultBehaviourNullResult() {
		final RbacMappingSupplier cachedSupplier = () -> null;

		final CachingSupplier supplier = new CachingSupplier(cachedSupplier);

		final Map<String, Set<String>> result = supplier.get();

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testCachingFunctionality() {
		final CachingSupplier supplier = new CachingSupplier(HashMap::new);

		final Map<String, Set<String>> result1 = supplier.get();
		final Map<String, Set<String>> result2 = supplier.get();

		assertThat(result1 == result2, is(true));
	}

}
