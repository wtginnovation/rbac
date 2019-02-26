package de.vsfexperts.rbac.spring.supplier;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supplier in order to log each invocation.
 */
public class LoggingSupplier implements RbacMappingSupplier {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingSupplier.class);

	private final Supplier<Map<String, Set<String>>> supplier;

	public LoggingSupplier(final Supplier<Map<String, Set<String>>> supplier) {
		requireNonNull(supplier, "supplier must not be null");
		this.supplier = supplier;
		logMapping();
	}

	@Override
	public Map<String, Set<String>> get() {
		return supplier.get();
	}

	private void logMapping() {
		final Map<String, Set<String>> result = supplier.get();

		if (result == null) {
			return;
		}

		final String mapping = result.entrySet().stream().map(e -> e.getKey() + " : " + e.getValue())
				.collect(Collectors.joining("\n"));

		LOG.info("Mapping of roles:\n{}", mapping);
	}

}
