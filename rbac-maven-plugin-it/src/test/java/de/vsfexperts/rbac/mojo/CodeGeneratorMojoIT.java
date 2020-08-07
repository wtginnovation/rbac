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

public class CodeGeneratorMojoIT {

	private static final String LOCATION_PRIVILEGES = "target/generated-sources/rbac/dummy/P.java";
	private static final String LOCATION_ROLES = "target/generated-sources/rbac/dummy/R.java";

	@Test
	public void testExecution() throws Exception {
		final File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/generate");

		final Verifier verifier = new VerifierBuilder().withBaseDirectoy(testDir).build();

		try {
			verifier.executeGoals(asList("clean", "install"));
			verifyExecution(verifier);
		} finally {
			verifier.deleteArtifacts("de.vsfexperts.rbac.it");
		}
	}

	private void verifyExecution(final Verifier verifier) throws IOException {
		verifyRoles(verifier);
		verifyPrivileges(verifier);
	}

	private void verifyRoles(final Verifier verifier) throws IOException {
		verifier.assertFilePresent(LOCATION_ROLES);

		final String roles = readFile(verifier.getBasedir() + "/" + LOCATION_ROLES);
		assertThat(roles, allOf(containsString(ROLE_ADMIN), containsString(ROLE_USER)));
	}

	private void verifyPrivileges(final Verifier verifier) throws IOException {
		verifier.assertFilePresent(LOCATION_PRIVILEGES);

		final String privileges = readFile(verifier.getBasedir() + "/" + LOCATION_PRIVILEGES);
		assertThat(privileges, allOf(containsString(PRIVILEGE_LIST_ORDERS), containsString(PRIVILEGE_UPDATE_ORDERS),
				containsString(PRIVILEGE_DELETE_ORDERS)));
	}

}
