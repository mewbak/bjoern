package bjoern.pluginlib;

import java.util.Map;
import java.util.Map.Entry;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import bjoern.pluginlib.structures.Node;

public class GraphOperations {

	public final static String INSTR_CFLOW_EDGE = "NEXT_INSTR";
	public final static String ALOC_USE_EDGE = "ALOC_USE_EDGE";


	/**
	 * Add an edge from the instruction src to the instruction dst if it does
	 * not already exist.
	 * <p>
	 *
	 * @param src the source of the edge
	 * @param dst the destination of the edge
	 */
	public static void addEdge(Graph graph, Node src, Node dst, String edgeType)
	{
		for (Edge edge : src.getNode().getEdges(Direction.OUT,
				edgeType))
		{
			if (edge.getVertex(Direction.IN).equals(dst.getNode()))
			{
				return;
			}
		}
			graph.addEdge(0, src.getNode(), dst.getNode(), INSTR_CFLOW_EDGE);
	}

	public static Vertex addNode(Graph graph, Map<String, String> properties)
	{
		Vertex newVertex = graph.addVertex(0);

		for( Entry<String, String> entrySet : properties.entrySet())
		{
			newVertex.setProperty(entrySet.getKey(), entrySet.getValue());
		}
		return newVertex;
	}

}
