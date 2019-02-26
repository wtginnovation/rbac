package de.vsfexperts.rbac.graph;

import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getPrivilegeNames;
import static de.vsfexperts.rbac.configuration.domain.util.MappingTools.getRoleNames;
import static de.vsfexperts.rbac.graph.GraphTools.edge;
import static de.vsfexperts.rbac.graph.GraphTools.graph;
import static de.vsfexperts.rbac.graph.GraphTools.key;
import static de.vsfexperts.rbac.graph.GraphTools.node;
import static com.yworks.xml.graphml.ShapeTypeType.ELLIPSE;
import static com.yworks.xml.graphml.ShapeTypeType.RECTANGLE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.graphdrawing.graphml.xmlns.graphml.EdgeType;
import org.graphdrawing.graphml.xmlns.graphml.GraphType;
import org.graphdrawing.graphml.xmlns.graphml.GraphmlType;
import org.graphdrawing.graphml.xmlns.graphml.NodeType;

import de.vsfexperts.rbac.configuration.domain.Privilege;
import de.vsfexperts.rbac.configuration.domain.RoleMapping;

/**
 * Generate a graph (including yEd proprietary extensions) off a {@link Set} of
 * {@link RoleMapping}s
 */
public class GraphGenerator {

	public GraphmlType createGraph(final Set<RoleMapping> mappings) {
		final Set<String> roleNames = getRoleNames(mappings);
		final Set<String> privilegeNames = getPrivilegeNames(mappings);

		final Set<NodeType> allNodes = new HashSet<>();
		roleNames.stream().forEach(r -> allNodes.add(node(r, r, RECTANGLE, "#FFCC00")));
		privilegeNames.stream().forEach(p -> allNodes.add(node(p, p, ELLIPSE, "#99CC00")));

		final List<EdgeType> allEdges = new ArrayList<>();
		for (final RoleMapping mapping : mappings) {
			final String roleName = mapping.getRoleName();
			for (final Privilege privilege : mapping.getPrivileges()) {
				final String privilegeName = privilege.getName();
				allEdges.add(edge(roleName, privilegeName));
			}
		}

		final GraphType graph = graph();
		graph.getDataOrNodeOrEdge().addAll(allNodes);
		graph.getDataOrNodeOrEdge().addAll(allEdges);

		final GraphmlType graphml = new GraphmlType();
		graphml.getGraphOrData().add(graph);

		roleNames.stream().forEach(r -> graphml.getKey().add(key(r)));
		privilegeNames.stream().forEach(p -> graphml.getKey().add(key(p)));

		return graphml;
	}

}
