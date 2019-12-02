package de.vsfexperts.rbac.mojo;

import java.io.File;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;

public class VerifierBuilder {

	private File baseDirectory;

	public VerifierBuilder withBaseDirectoy(final File baseDirectory) throws Exception {
		this.baseDirectory = baseDirectory;
		return this;
	}

	public Verifier build() throws VerificationException {
		final String jacocoArguments = System.getProperty("jacocoArgs", "");

		final Verifier verifier = new Verifier(baseDirectory.getAbsolutePath());
		verifier.setEnvironmentVariable("MAVEN_OPTS", jacocoArguments);
		verifier.setSystemProperty("jacocoArgs", jacocoArguments);

		return verifier;
	}

}