package de.vsfexperts.rbac.spring.supplier;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Marker interface to allow autowiring by type reliantly. Clients should be
 * using the supplier instead in order to be more flexible. So use Supplier<...>
 * instead of requiring an instance of this class.
 */
@FunctionalInterface
public interface RbacMappingSupplier extends Supplier<Map<String, Set<String>>> {

}
