package de.vsfexperts.rbac.spring.supplier;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Simple supplier/proxy in order to cache the results. Wrapped/Cached supplier
 * is only fetched once during initialization of class.
 */
public class CachingSupplier implements RbacMappingSupplier {

	private final Map<String, Set<String>> cache;

	public CachingSupplier(final Supplier<Map<String, Set<String>>> supplier) {
		requireNonNull(supplier, "supplier must not be null");
		cache = supplier.get();
	}

	@Override
	public Map<String, Set<String>> get() {
		return cache;
	}

}
