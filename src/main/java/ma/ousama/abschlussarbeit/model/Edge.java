package ma.ousama.abschlussarbeit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * @author Masud Taher
 * @version 1.0
 */

public class Edge {
	private Node start; // pi
	private Node end; // pj

	@JsonIgnore
	private Edge e_transformed;

	public Edge() {
	}

	public Edge(Node start, Node end) {
		this.start = start;
		this.end = end;
		this.e_transformed = new Edge();
		this.e_transformed.start = start;
		this.e_transformed.end = end;
	}

	public Edge(Node start, Node end, Edge e_transformed) {
		this.start = start;
		this.end = end;
		this.e_transformed = e_transformed;
	}

	@JsonIgnore public Edge getE_transformed() {
		return e_transformed;
	}

	@JsonIgnore public void setE_transformed(Edge e_transformed) {
		this.e_transformed = e_transformed;
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	public static double getDistance(Node start, Node end) { // start= (Xs,Ys) ende = (Xe,Ye)
		double x = Math.pow(Math.abs(end.getX() - start.getX()), 2);
		double y = Math.pow(Math.abs(end.getY() - start.getY()), 2);
		return Math.sqrt(x + y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Edge edge = (Edge) o;
		boolean e_transformedB = false;
		if (e_transformed.getStart().equals(((Edge) o).getStart())
				&& e_transformed.getEnd().equals(((Edge) o).getEnd()))
			e_transformedB = true;

		return Objects.equals(start, edge.start) && Objects.equals(end, edge.end) && e_transformedB;
	}

	@Override
	public int hashCode() {
		return Objects.hash(start, end, e_transformed);
	}

	@Override
	public String toString() {
		return "(" + start.getX() + ", " + start.getY() + ") , (" + end.getX() + ", " + end.getY() + ")";
	}
}