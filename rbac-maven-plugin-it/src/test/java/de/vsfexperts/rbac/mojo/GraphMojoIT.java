package de.vsfexperts.rbac.mojo;

import static de.vsfexperts.rbac.mojo.FileTools.readFile;
import static de.vsfexperts.rbac.mojo.TestData.PRIVILEGE_DELETE_ORDERS;
import static de.vsfexperts.rbac.mojo.TestData.PRIVILEGE_LIST_ORDERS;
import static de.vsfexperts.rbac.mojo.TestData.PRIVILEGE_UPDATE_ORDERS;
import static de.vsfexperts.rbac.mojo.TestData.ROLE_ADMIN;
import static de.vsfexperts.rbac.mojo.TestData.ROLE_USER;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;

public class GraphMojoIT {

	private static String LOCATION_GRAPH = "target/generated-sources/rbac/report.graphml";

	@Test
	public void testExecution() throws Exception {
		final File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/graph");

		final Verifier verifier = new VerifierBuilder().withBaseDirectoy(testDir).build();

		try {
			verifier.executeGoals(asList("clean", "install"));
			verifyGraph(verifier);
		} finally {
			verifier.deleteArtifacts("de.vsfexperts.rbac.it");
		}

	}

	private void verifyGraph(final Verifier verifier) throws IOException {

		verifier.assertFilePresent(LOCATION_GRAPH);

		final String graph = readFile(verifier.getBasedir() + "/" + LOCATION_GRAPH);
		assertThat(graph, allOf(containsString(ROLE_ADMIN), containsString(ROLE_USER)));
		assertThat(graph, allOf(containsString(PRIVILEGE_LIST_ORDERS), containsString(PRIVILEGE_UPDATE_ORDERS),
				containsString(PRIVILEGE_DELETE_ORDERS)));
	}

}
