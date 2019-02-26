package de.vsfexperts.rbac.configuration.domain.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;

/**
 * Generic comparator in order to compare 2 arbitrary collections.
 *
 * @param <T> type of collection
 */
public class CollectionComparator<T> implements Comparator<Collection<T>>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(final Collection<T> c1, final Collection<T> c2) {
		if (c1 == c2) {
			return 0;
		}

		for (final T elem1 : c1) {
			if (!c2.contains(elem1)) {
				return -1;
			}
		}

		return c2.size() - c1.size();

	}

}
