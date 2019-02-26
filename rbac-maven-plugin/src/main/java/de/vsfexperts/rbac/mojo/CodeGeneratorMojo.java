package de.vsfexperts.rbac.mojo;

import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import de.vsfexperts.rbac.code.ClassGenerator;
import de.vsfexperts.rbac.code.PrivilegeFieldSupplier;
import de.vsfexperts.rbac.code.RoleFieldSupplier;
import de.vsfexperts.rbac.configuration.domain.RoleMapping;

/**
 * Code generator Mojo, which is creating the Roles & Privileges java classes.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true, threadSafe = true)
public class CodeGeneratorMojo extends BaseMojo {

	/**
	 * Package of generated classes
	 */
	@Parameter(property = "packageName", required = true)
	private String packageName;

	/**
	 * Created privileges should be compatible with spring (=each constant is
	 * prefixed with ROLE_)
	 */
	@Parameter(property = "springRoles", defaultValue = "true")
	private boolean springRoles;

	/**
	 * Classname of generated privileges class
	 */
	@Parameter(property = "privilegeClassName", required = true, defaultValue = "Privileges")
	private String privilegeClassName;

	/**
	 * Classname of generated roles class
	 */
	@Parameter(property = "roleClassName", required = true, defaultValue = "Roles")
	private String roleClassName;

	@Override
	protected void execute(final Set<RoleMapping> mappings)
			throws MojoExecutionException, MojoFailureException {

		getLog().info("Creating classes in " + getOutputDirectory().getAbsolutePath());
		createPrivilegesClass(mappings);
		createRolesClass(mappings);
	}

	private void createPrivilegesClass(final Set<RoleMapping> mappings) throws MojoFailureException {
		final PrivilegeFieldSupplier fieldSupplier = new PrivilegeFieldSupplier(mappings, springRoles);

		try {
			new ClassGenerator(packageName, privilegeClassName) //
					.withFields(fieldSupplier) //
					.generateIn(getOutputDirectory());
		} catch (final Exception e) {
			throw new MojoFailureException("Error in creation of class " + privilegeClassName, e);
		}
	}

	private void createRolesClass(final Set<RoleMapping> mappings) throws MojoFailureException {
		final RoleFieldSupplier fieldSupplier = new RoleFieldSupplier(mappings);

		try {
			new ClassGenerator(packageName, roleClassName) //
					.withFields(fieldSupplier) //
					.generateIn(getOutputDirectory());
		} catch (final Exception e) {
			throw new MojoFailureException("Error in creation of class " + roleClassName, e);
		}
	}

}
