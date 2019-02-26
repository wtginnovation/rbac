package de.vsfexperts.rbac.configuration.domain.util;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import de.vsfexperts.rbac.configuration.domain.util.CollectionComparator;

public class CollectionComparatorTest {

	private CollectionComparator<String> comparator;

	@Before
	public void setUp() {
		comparator = new CollectionComparator<>();
	}

	@Test
	public void testEqual() {
		final SortedSet<String> ss1 = new TreeSet<>(asList("1", "2", "3"));
		final SortedSet<String> ss2 = new TreeSet<>(asList("1", "2", "3"));

		assertThat(comparator.compare(ss1, ss2), is(0));
	}

	@Test
	public void testEqualNull() {
		assertThat(comparator.compare(null, null), is(0));
	}

	@Test
	public void testEqualSameCollection() {
		final SortedSet<String> ss1 = new TreeSet<>(asList("1", "2", "3"));

		assertThat(comparator.compare(ss1, ss1), is(0));
	}

	@Test
	public void testEqualEmpty() {
		final SortedSet<String> ss1 = new TreeSet<>();
		final SortedSet<String> ss2 = new TreeSet<>();

		assertThat(comparator.compare(ss1, ss2), is(0));
	}

	@Test
	public void testNonEqual() {
		final SortedSet<String> ss1 = new TreeSet<>(asList("1", "2"));
		final SortedSet<String> ss2 = new TreeSet<>(asList("1"));

		assertThat(comparator.compare(ss1, ss2) < 0, is(true));
	}

	@Test
	public void testNonEqualReversed() {
		final SortedSet<String> ss1 = new TreeSet<>(asList("1"));
		final SortedSet<String> ss2 = new TreeSet<>(asList("1", "2"));

		assertThat(comparator.compare(ss1, ss2) > 0, is(true));
	}

	@Test
	public void testNonEqualDifferent() {
		final SortedSet<String> ss1 = new TreeSet<>(asList("1"));
		final SortedSet<String> ss2 = new TreeSet<>(asList("2"));

		assertThat(comparator.compare(ss1, ss2) < 0, is(true));
	}

}
