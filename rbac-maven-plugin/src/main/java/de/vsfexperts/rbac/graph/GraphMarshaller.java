package de.vsfexperts.rbac.graph;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.graphdrawing.graphml.xmlns.graphml.GraphmlType;

import com.yworks.xml.graphml.ShapeNodeType;

/**
 * Serialize graph to {@link String} (via jaxb)
 */
public class GraphMarshaller {

	public String marshal(final GraphmlType graph) throws JAXBException {
		final String packages = GraphmlType.class.getPackage().getName() + ":"
				+ ShapeNodeType.class.getPackage().getName();
		final StringWriter writer = new StringWriter();

		final Marshaller marshaller = JAXBContext.newInstance(packages).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(graph, writer);

		return writer.toString();
	}

}
