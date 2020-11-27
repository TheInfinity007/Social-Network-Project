/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import util.GraphLoader;

/**
 * @author Ram Chandra
 *
 */

public class CapGraph implements Graph {

	private static final boolean Node = false;
	private HashMap<Integer, Node> integerNodeMap;
	private HashSet<Edge> edges;
	
	public CapGraph(){
		integerNodeMap = new HashMap<Integer, Node>();
	}
	
	@Override
	public void addVertex(int num) {
		Node node = integerNodeMap.get(num);
		if(node == null) {
			integerNodeMap.put(num, new Node(num));
		}
	}

	@Override
	public void addEdge(int from, int to) {
		Node start = integerNodeMap.get(from);
		Node end = integerNodeMap.get(to);
		
		if(start == null || end == null)
			return;
		
		Edge edge = new Edge(start, end);
		start.addEdge(edge);
	}

	@Override
	public CapGraph getEgonet(int center) {
		CapGraph g = new CapGraph();
		
		Node node = integerNodeMap.get(center);
		if(node == null) {
			return g;
		}
		
		HashSet<Integer> seen = new HashSet<Integer>();
		ArrayList<Integer> neighbors = node.getNeighbors();
		
		g.addVertex(center);
		
		for(Integer val: neighbors) {
			g.addVertex(val);
			g.addEdge(center, val);
			seen.add(val);
		}
		
		for(Integer neighbor: neighbors) {
			ArrayList<Integer> neighbors2 = this.getIntegerNodeMap().get(neighbor).getNeighbors();
			for(Integer n: neighbors2) {
				if(neighbors.contains(n)) {
					g.addEdge(neighbor, n);
				}
			}
			if(this.getIntegerNodeMap().get(neighbor).isNeighbor(center)) {
				g.addEdge(neighbor, center);
			}
		}
//		System.out.println(g.getIntegerNodeMap().keySet().size());
		return g;
	}

	@Override
	public List<Graph> getSCCs() {
		Set<Integer> nodes = getIntegerNodeMap().keySet();
		Stack<Integer> vertices = new Stack<Integer>();
		
		vertices.addAll(nodes);
//		System.out.println("Vertices " + vertices);
		Stack<Integer> finished = DFS(this, vertices);
//		System.out.println("Finished " + finished);
		CapGraph transpose = transpose(this);	
		
		Stack<Integer> finished2 = new Stack<Integer>();
		finished2.addAll(finished);
		
		Stack<Integer> result = DFS(transpose, finished);
//		System.out.println(result);		
		
		List<Graph> list = new ArrayList<Graph>();
		SCCUtil(this, transpose, finished2, list);
		
		return list;
	}
	
	
	private void SCCUtil(CapGraph source, CapGraph g, Stack<Integer> vertices, List<Graph> list) {
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		while(vertices.size() != 0) {
			Integer v = vertices.pop();
//			System.out.print(v + " ");
			if(!visited.contains(v)) {
				DFS_VISIT(g, v, visited, finished);
				ArrayList<Integer> res = new ArrayList<Integer>(finished);
				finished = new Stack<Integer>();
				list.add(createGraphFromVertex(source, res));
			}
//			System.out.println(finished);
		}
		
	}

	private CapGraph createGraphFromVertex(CapGraph g, ArrayList<Integer> res) {
		CapGraph graph = new CapGraph();
		for(Integer i: res) {
			graph.addVertex(i);
		}
		for(Integer i: res) {
			ArrayList<Integer> neighbors = g.getIntegerNodeMap().get(i).getNeighbors();
			
			for(Integer node : neighbors) {
				if(res.contains(node)) {
					graph.addEdge(i, node);
				}
			}
		}
		return graph;
	}

	public CapGraph transpose(CapGraph graph) {
		ArrayList<Integer> vertices = new ArrayList<>(graph.getIntegerNodeMap().keySet());
		CapGraph g = new CapGraph();
		HashSet<Integer> seen = new HashSet<Integer>();
		
		while(vertices.size() != 0) {
			Integer v = vertices.remove(0);
			if(!seen.contains(v)) {
				seen.add(v);
				g.addVertex(v);
			}
			Node node = graph.getIntegerNodeMap().get(v);
			ArrayList<Integer> neighbors = node.getNeighbors();
			for(Integer n: neighbors) {
				if(!seen.contains(n)) {
					seen.add(n);
					g.addVertex(n);
				}
				g.addEdge(n, v);
			}
		}
		return g;
	}
	
	public Stack<Integer> DFS(CapGraph g, Stack<Integer> vertices){
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		while(vertices.size() != 0) {
			Integer v = vertices.pop();
			if(!visited.contains(v)) {
//				System.out.print(v + " ");
				DFS_VISIT(g, v, visited, finished);
//				System.out.println(finished);
			}
		}
		return finished;
	}


	private void DFS_VISIT(CapGraph g, Integer v, HashSet<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		ArrayList<Integer> neighbors = g.integerNodeMap.get(v).getNeighbors();
		for(Integer n: neighbors) {
			if(!visited.contains(n)) {
				DFS_VISIT(g, n, visited, finished);
			}
		}
		finished.push(v);
	}

	
	// return all its vertices: neighbors of vertices
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		HashMap<Integer, HashSet<Integer>> res = new HashMap<Integer, HashSet<Integer>>();
		
		ArrayList<Integer> vertices = new ArrayList<Integer>(this.getIntegerNodeMap().keySet());
//		System.out.println("Vertices " + vertices);
		
		for(Integer i: vertices) {
			HashSet<Integer> neighbors = new HashSet<Integer>(this.getIntegerNodeMap().get(i).getNeighbors());
			res.put(i,  neighbors);
		}
		
		return res;
	}
	
	public Graph getMaximumSCC() {
		CapGraph maxSCC = null;
		int max = 0;
		
		List<Graph> list = this.getSCCs();
		for(Graph g: list) {
			CapGraph graph = (CapGraph)g;
			if(graph.getIntegerNodeMap().keySet().size() > max) {
				maxSCC = graph;
				max = graph.getIntegerNodeMap().keySet().size();
			}		
		}		
		return maxSCC; 
	}
	
	public int getminimumBroadcastUsers() {
		ArrayList<Integer> dominant = getBroadcastUsers();
		return dominant.size();
	}
	
	
	public ArrayList<Integer> getBroadcastUsers() {
		ArrayList<Integer> dominant = new ArrayList<Integer>();
		
		List<Node> uncoveredNode = new ArrayList<Node>();
		Set<Integer> uncovered = new HashSet<Integer>(this.getIntegerNodeMap().keySet());
		
		for(Integer key: this.getIntegerNodeMap().keySet()) {
			uncoveredNode.add((this.getIntegerNodeMap()).get(key));
		}
		Collections.sort(uncoveredNode);
//		System.out.println(uncoveredNode);
		
		while(uncovered.size() > 0 && uncoveredNode.size() > 0) {
			Node max = uncoveredNode.remove(0);
			int val = max.getValue();
			if(!(uncovered.contains(val)))
				continue;
			dominant.add(val);
			uncovered.remove(val);
			ArrayList<Integer> neighbors = max.getNeighbors();
			for(Integer n: neighbors) {
				uncovered.remove(n);
			}
		}
//		System.out.println(uncoveredNode);
//		System.out.println(uncovered);
	
		return dominant;
	}
	
	public List<Integer> getMinimumUsersDifference(int user1, int user2){
		List<Integer> res = dfs(user1, user2);
		return res;
	}
	
	private List<Integer> dfs(int start, int end) {
		Set<Integer> visited = new HashSet<Integer>();
		Queue<Integer>queue = new LinkedList<Integer>();
		HashMap<Integer, Integer>parentMap = new HashMap<Integer, Integer>();
		
		Node s = this.getIntegerNodeMap().get(start);
		Node e = this.getIntegerNodeMap().get(end);
		if(s == null || e == null) {
			throw new IllegalArgumentException("Invalid Input values not exist");
		}
		boolean found = false;
		
		queue.add(start);
		while(queue.size() != 0) {
			int val = queue.poll();
			if(val == end) {
				found = true;
				break;
			}
			if(!visited.contains(val)) {
				visited.add(val);
				for(Integer n : this.getIntegerNodeMap().get(val).getNeighbors()) {
					if(!visited.contains(n)) {
						queue.add(n);
						if(!parentMap.containsKey(n)) parentMap.put(n, val);
					}
				}
			}
		}
		
		if(!found)	return new ArrayList<Integer>();
		
		List<Integer> path = reconstructPath(parentMap, start, end);
		
		return path;
	}

	private List<Integer> reconstructPath(HashMap<Integer, Integer> parentMap, int start, int end) {
		List<Integer> path = new LinkedList<Integer>();
		Integer curr = end;
		while(curr != start) {
			path.add(0, curr);
			curr = parentMap.get(curr);
		}
		path.add(0, start);
		return path;
	}

	public static void main(String[] args) {
		CapGraph graph =  new CapGraph();
		GraphLoader.loadGraph(graph, "data/twitter_combined_7000.txt");
		
		System.out.println("Original Graph");
		System.out.println(graph.getIntegerNodeMap());
//		for(Integer key: graph.getIntegerNodeMap().keySet()) {
//			System.out.println(key + " " + graph.getIntegerNodeMap().get(key).getNeighbors());
//		}
		

		System.out.println("\nEgonet Graph of 276308596");
		CapGraph g = new CapGraph();
		g = graph.getEgonet(276308596);
		
		System.out.println("Size of egonet graph: " + g.getIntegerNodeMap().keySet().size());
		System.out.println("Egonet graph nodes: " + g.getIntegerNodeMap().keySet());
//		for(Integer key: g.getIntegerNodeMap().keySet()) {
//			System.out.println(key + " " + g.getIntegerNodeMap().get(key).getNeighbors());
//		}
		
		System.out.println("\nSCC Graph");
		graph.getSCCs();
		System.out.println("Export Graph");
		graph.exportGraph();
		
		CapGraph maxScc = (CapGraph) graph.getMaximumSCC();
		System.out.println("\nMaxSCC: ");
		System.out.println("MaxSCC graph size: " + maxScc.getIntegerNodeMap().keySet().size());
		System.out.println("MaxSCC graph nodes: " + maxScc.getIntegerNodeMap().keySet());
		
		CapGraph graph2 = new CapGraph();
		GraphLoader.loadGraph(graph2, "data/twitter_combined_7000.txt");
		System.out.println("\nMinimum Broadcast Users");
		System.out.println("Total Users " + graph2.getIntegerNodeMap().keySet().size());
		System.out.println("Total Relations or edges " + 7000);
//		for(Integer key: graph2.getIntegerNodeMap().keySet()) {
//			System.out.println(key + " " + graph2.getIntegerNodeMap().get(key).getNeighbors() + " " + graph2.getIntegerNodeMap().get(key).getEdges().size());
//		}
		System.out.println("Minimum number of broadcast users: " + graph2.getminimumBroadcastUsers());
		System.out.println("List of broadcast users: " + graph2.getBroadcastUsers());

		System.out.println("\nMinimum Distance between users ");
		try {
			System.out.println(graph2.getMinimumUsersDifference(153226312, 394263193));
			System.out.println(graph2.getMinimumUsersDifference(153226312, 17627996));
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
		System.out.println("End");
	}

	
	
	
	public HashMap<Integer, Node> getIntegerNodeMap() {
		return integerNodeMap;
	}

	public void setIntegerNodeMap(HashMap<Integer, Node> integerNodeMap) {
		this.integerNodeMap = integerNodeMap;
	}

}
