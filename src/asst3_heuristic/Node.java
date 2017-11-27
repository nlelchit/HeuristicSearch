package asst3_heuristic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node {
	List<Node> neighbors = new ArrayList<Node>();
	Node parent;
	double f = 0;
	double g = 0;
	double h = 0;
	int x;
	int y;
	double cost;

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Node() {
		x = 0;
		y = 0;
	}
	
	public Node(Node n){
		this.x = n.x;
		this.y = n.y;
	}

	public String toString() {
		return "[" + x + "," + y + "]";
	}
	

	public boolean equals(Object object) {
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			Node n = (Node) object;
			if (this.x == n.x && this.y == n.y) {
				result = true;
			}
		}
		return result;
	}

}
