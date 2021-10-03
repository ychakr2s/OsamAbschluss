package ma.ousama.abschlussarbeit.model;

import java.util.LinkedList;

/**
 * @author Masud Taher
 * @version 1.0
 */

public class Graph {

	private LinkedList<Node> v;
	private LinkedList<Edge> e;


	public Graph(Graph g) {
		this.v = g.getV();
		this.e = g.getE();
		
	}

	public Graph(LinkedList<Node> v, LinkedList<Edge> e) {
		this.v = v;
		this.e = e;
		
	}

	public LinkedList<Node> getV() {
		return v;
	}

	public LinkedList<Edge> getE() {
		return e;
	}

	public int getVSize() {
		return v.size();
	}

	public int getESize() {
		return e.size();
	}

	@Override
	public String toString() {
		String res = "";
		for (int i = 0; i < v.size(); i++) {
			res = res + v.get(i).toString() + "\n";
		}
		res = res + "\n";
		for (int i = 0; i < e.size(); i++) {
			res = res + e.get(i).toString() + "\n";
		}
		return res;
	}
}