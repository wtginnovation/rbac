package de.vsfexperts.rbac.graph;

import org.graphdrawing.graphml.xmlns.graphml.DataType;
import org.graphdrawing.graphml.xmlns.graphml.EdgeType;
import org.graphdrawing.graphml.xmlns.graphml.GraphEdgedefaultType;
import org.graphdrawing.graphml.xmlns.graphml.GraphType;
import org.graphdrawing.graphml.xmlns.graphml.KeyForType;
import org.graphdrawing.graphml.xmlns.graphml.KeyType;
import org.graphdrawing.graphml.xmlns.graphml.NodeType;

import com.yworks.xml.graphml.NodeLabelType;
import com.yworks.xml.graphml.NodeType.Fill;
import com.yworks.xml.graphml.ShapeNodeType;
import com.yworks.xml.graphml.ShapeNodeType.Shape;
import com.yworks.xml.graphml.ShapeTypeType;

/**
 * Common set of tools to create parts of a graphml file (e.g. node, edge)
 */
public class GraphTools {

	public static GraphType graph() {
		final GraphType graph = new GraphType();
		graph.setId("G");
		graph.setEdgedefault(GraphEdgedefaultType.DIRECTED);

		return graph;
	}

	public static NodeType node(final String id, final String descr, final ShapeTypeType shapeType,
			final String backgroundColor) {

		final NodeLabelType label = new NodeLabelType();
		label.setValue(descr);

		final Shape shape = new Shape();
		shape.setType(shapeType);

		final Fill fill = new Fill();
		fill.setColor(backgroundColor);
		fill.setTransparent(Boolean.FALSE);

		final ShapeNodeType shapeNode = new ShapeNodeType();
		shapeNode.getNodeLabel().add(label);
		shapeNode.setFill(fill);
		shapeNode.setShape(shape);

		final DataType data = new DataType();
		data.setKey("D_" + id);
		data.getContent().add(shapeNode);

		final NodeType node = new NodeType();
		node.setId(id);
		node.setDesc(descr);
		node.getDataOrPort().add(data);

		return node;
	}

	public static EdgeType edge(final String src, final String target) {
		final EdgeType edge = new EdgeType();
		edge.setSource(src);
		edge.setTarget(target);

		return edge;
	}

	public static KeyType key(final String id) {
		final KeyType key = new KeyType();
		key.setId("D_" + id);
		key.setFor(KeyForType.NODE);
		key.setYfilesType("nodegraphics");

		return key;
	}

}
