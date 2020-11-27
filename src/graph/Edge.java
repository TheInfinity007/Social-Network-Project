package graph;

public class Edge {

	Node start;
	Node end;
	
	public Edge() {
	}
	
	public Edge(Node start, Node end) {
		this.start = start;
		this.end = end;
	}
	
	public Node getOtherNode(Node node) {
		if(node.equals(start)) {
			return end;
		}
		if(node.equals(end)) {
			return start;
		}
		throw new IllegalArgumentException("Lookin for a node which is not in the edge.");
	}

	/**
	 * @return the start
	 */
	public Node getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Node start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Node end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Edge [start=" + start + ", end=" + end + "]";
	}

}
