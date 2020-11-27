package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node implements Comparable{
	private int value;
	private HashSet<Edge> edges;

	Node() {
	}

	Node(int value) {
		this.value = value;
		edges = new HashSet<Edge>();
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	// return all the neighbors this node
	public ArrayList<Integer> getNeighbors(){
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		for(Edge edge: edges) {
			neighbors.add(edge.getOtherNode(this).value);
		}
		return neighbors;
	}
	
	public boolean isNeighbor(Integer val) {
		ArrayList<Integer> neighbors = getNeighbors();
		for(Integer neighbor: neighbors) {
			if(neighbor == val)
				return true;
		}
		return false;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the edges
	 */
	public HashSet<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Node " + value;
	}

	@Override
	public int compareTo(Object o) {
		Node node = (Node)o;
		return node.edges.size() - this.getEdges().size();
	}
}
