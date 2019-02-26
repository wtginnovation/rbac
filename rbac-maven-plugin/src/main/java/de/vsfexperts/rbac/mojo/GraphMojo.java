package de.vsfexperts.rbac.mojo;

import static org.codehaus.plexus.util.FileUtils.fileWrite;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.graphdrawing.graphml.xmlns.graphml.GraphmlType;

import de.vsfexperts.rbac.configuration.domain.RoleMapping;
import de.vsfexperts.rbac.graph.GraphGenerator;
import de.vsfexperts.rbac.graph.GraphMarshaller;

/**
 * Graph generator, which is creating a graph (in graphml format) of the role
 * and privileges.
 */
@Mojo(name = "graph", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresProject = true, threadSafe = true)
public class GraphMojo extends BaseMojo {

	/**
	 * location of generated graph (relative to output folder)
	 */
	@Parameter(property = "reportFileName", defaultValue = "META-INF/role-mapping.graphml")
	protected String reportFileName;

	@Override
	protected void execute(final Set<RoleMapping> mappings) throws MojoFailureException {

		final File graphFile = new File(getOutputDirectory().getAbsolutePath() + File.separatorChar + reportFileName);
		createDirectory(graphFile.getParentFile());

		final String graph = createGraph(mappings);
		storeGraph(graph, graphFile);
	}

	private String createGraph(final Set<RoleMapping> mappings) throws MojoFailureException {
		try {
			final GraphmlType graph = new GraphGenerator().createGraph(mappings);
			return new GraphMarshaller().marshal(graph);
		} catch (final JAXBException e) {
			throw new MojoFailureException("Serialization of graph has failed", e);
		}
	}

	private void storeGraph(final String graph, final File graphFile) throws MojoFailureException {
		getLog().info("Creating graph in " + graphFile.getAbsolutePath());

		try {
			fileWrite(graphFile, graph);
		} catch (final IOException e) {
			throw new MojoFailureException("Couldn't create graph file", e);
		}
	}

}
