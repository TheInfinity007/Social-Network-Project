package graph;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import util.GraphLoader;

public class GraphTester2 {

CapGraph graph;
	
	@Before
	public void setUp() throws Exception {
		graph =  new CapGraph();
		GraphLoader.loadGraph(graph, "data/small_test_graph.txt");
	}
	
	
	@Test
	public void testSize() {
		assertEquals("Check Node Size of small_test_graph.txt", 14, graph.getIntegerNodeMap().size());
		assertEquals("No of neighbors of node 1", 2, graph.getIntegerNodeMap().get(2).getEdges().size());
	}
	
	@Test
	public void testGet() {
		HashSet <Edge> edges = graph.getIntegerNodeMap().get(2).getEdges();
		Node n = edges.iterator().next().getOtherNode(graph.getIntegerNodeMap().get(2));
		assertEquals("Neighbors value of 2", 1, n.getValue());
	}

}
