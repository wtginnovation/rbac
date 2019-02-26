package de.vsfexperts.rbac.mojo;

import static org.codehaus.plexus.util.FileUtils.forceMkdir;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.FileSystemResource;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import de.vsfexperts.rbac.configuration.io.YamlLoader;

/**
 * Base class of all mojos, which is providing some common runtime checks (e.g.
 * guaranteed creation/existence of base output folder)
 */
public abstract class BaseMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	/**
	 * location of rbac mapping/configuration file
	 */
	@Parameter(property = "rbacFile", defaultValue = "${basedir}/src/main/resources/rbac.yaml")
	private File rbacFile;

	/**
	 * output folder
	 */
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/rbac")
	private File outputDirectory;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		validateInputs();

		createDirectory(outputDirectory);

		if (requiresExecution()) {
			final Set<RoleMapping> mappings = loadRoleMappings();
			execute(mappings);
		}

		registerOutputFolderAsSourceFolder();
	}

	protected abstract void execute(Set<RoleMapping> mappings) throws MojoExecutionException, MojoFailureException;

	private void validateInputs() throws MojoFailureException {
		if (!rbacFile.exists() || rbacFile.isDirectory()) {
			throw new MojoFailureException(
					"Rbac configuration must be provided. File " + rbacFile.getAbsolutePath() + " might not exist.");
		}
	}

	protected final void createDirectory(final File directory) throws MojoFailureException {
		try {
			forceMkdir(directory);
		} catch (final IOException e) {
			throw new MojoFailureException("Failed to create directory: " + directory, e);
		}
	}

	private boolean requiresExecution() throws MojoFailureException, MojoExecutionException {
		try {
			final File targetDirectory = new File(project.getBuild().getDirectory());
			createDirectory(targetDirectory);

			final String mojoName = this.getClass().getSimpleName().toLowerCase();

			final File lastModifiedTracker = new File(targetDirectory, "rbac-" + mojoName + ".timestamp");

			final boolean requiresExecution;
			if (lastModifiedTracker.exists()) {
				requiresExecution = rbacFile.lastModified() != lastModifiedTracker.lastModified();
			} else {
				lastModifiedTracker.createNewFile();
				requiresExecution = true;
			}

			final boolean successfulUpdate = lastModifiedTracker.setLastModified(rbacFile.lastModified());
			if (!successfulUpdate) {
				throw new MojoExecutionException("Couldn't update timestamp of " + rbacFile.getAbsolutePath());
			}

			return requiresExecution;

		} catch (final MojoFailureException e) {
			throw e;
		} catch (final Exception e2) {
			throw new MojoExecutionException(e2.getMessage(), e2);
		}
	}

	private Set<RoleMapping> loadRoleMappings() throws MojoFailureException {
		final Set<RoleMapping> mappings;
		try {
			mappings = new YamlLoader().load(new FileSystemResource(rbacFile));
		} catch (final IOException e) {
			throw new MojoFailureException("Error loading rbac config", e);
		}
		return mappings;
	}

	private void registerOutputFolderAsSourceFolder() {
		project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
	}

	protected final File getOutputDirectory() {
		return outputDirectory;
	}

}
