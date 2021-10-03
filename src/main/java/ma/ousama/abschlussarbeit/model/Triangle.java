package ma.ousama.abschlussarbeit.model;

import ma.ousama.abschlussarbeit.delaunayTriangulierung.EdgeDistancePack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Triangle {

    private Node a;
    private Node b;
    private Node c;


    public Node getA() {
        return a;
    }
    public Node getB() {
        return b;
    }
    public Node getC() {
        return c;
    }

    public void setA(Node a) {
        this.a = a;
    }

    public void setB(Node b) {
        this.b = b;
    }

    public void setC(Node c) {
        this.c = c;
    }

    public Triangle(Node a, Node b, Node c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean contains(Node point) {
        double pab = point.sub(a).cross(b.sub(a));
        double pbc = point.sub(b).cross(c.sub(b));

        if (!hasSameSign(pab, pbc)) {
            return false;
        }

        double pca = point.sub(c).cross(a.sub(c));

        if (!hasSameSign(pab, pca)) {
            return false;
        }

        return true;
    }

    public boolean isPointInCircumcircle(Node point) {
        double a11 = a.getX() - point.getX();
        double a21 = b.getX() - point.getX();
        double a31 = c.getX() - point.getX();

        double a12 = a.getY() - point.getY();
        double a22 = b.getY() - point.getY();
        double a32 = c.getY() - point.getY();

        double a13 = (a.getX() - point.getX()) * (a.getX() - point.getX()) + (a.getY() - point.getY()) * (a.getY() - point.getY());
        double a23 = (b.getX() - point.getX()) * (b.getX() - point.getX()) + (b.getY() - point.getY()) * (b.getY() - point.getY());
        double a33 = (c.getX() - point.getX()) * (c.getX() - point.getX()) + (c.getY() - point.getY()) * (c.getY()- point.getY());

        double det = a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33
                - a11 * a23 * a32;

        if (isOrientedCCW()) {
            return det > 0.0d;
        }

        return det < 0.0d;
    }

    public boolean isOrientedCCW() {
        double a11 = a.getX() - c.getX();
        double a21 = b.getX() - c.getX();

        double a12 = a.getY() - c.getY();
        double a22 = b.getY() - c.getY();

        double det = a11 * a22 - a12 * a21;

        return det > 0.0d;
    }

    public boolean isNeighbour(Edge edge) {
        return (a == edge.getStart() || b == edge.getStart() || c == edge.getStart()) && (a == edge.getEnd() || b == edge.getEnd() || c == edge.getEnd());
    }

    public Node getNoneEdgeVertex(Edge edge) {
        if (a != edge.getStart() && a != edge.getEnd()) {
            return a;
        } else if (b != edge.getStart() && b != edge.getEnd()) {
            return b;
        } else if (c != edge.getStart() && c != edge.getEnd()) {
            return c;
        }

        return null;
    }

    public boolean hasVertex(Node node) {
        if (a == node || b == node || c == node) {
            return true;
        }
        return false;
    }

    public EdgeDistancePack findNearestEdge(Node point) {
        EdgeDistancePack[] edges = new EdgeDistancePack[3];

        edges[0] = new EdgeDistancePack(new Edge(a, b),
                computeClosestPoint(new Edge(a, b), point).sub(point).mag());
        edges[1] = new EdgeDistancePack(new Edge(b, c),
                computeClosestPoint(new Edge(b, c), point).sub(point).mag());
        edges[2] = new EdgeDistancePack(new Edge(c, a),
                computeClosestPoint(new Edge(c, a), point).sub(point).mag());

        Arrays.sort(edges);
        return edges[0];
    }

    private Node computeClosestPoint(Edge edge, Node point) {
        Node ab = edge.getEnd().sub(edge.getStart());
        double t = point.sub(edge.getStart()).dot(ab) / ab.dot(ab);

        if (t < 0.0d) {
            t = 0.0d;
        } else if (t > 1.0d) {
            t = 1.0d;
        }

        return edge.getStart().add(ab.mult(t));
    }

    private boolean hasSameSign(double a, double b) {
        return Math.signum(a) == Math.signum(b);
    }

    public static LinkedList<Edge> convertTriangleToEdges(List<Triangle> triangles){

        LinkedList<Edge> result = new LinkedList <Edge> ();

        for ( int i = 0 ;  i < triangles.size() ;i++ ){
            Edge a =new Edge( triangles.get(i).getA(), triangles.get(i).getB());
            Edge aI =new Edge( triangles.get(i).getB(), triangles.get(i).getA());
            Edge b =new Edge( triangles.get(i).getB(), triangles.get(i).getC());
            Edge bI =new Edge( triangles.get(i).getC(), triangles.get(i).getB());
            Edge c =new Edge( triangles.get(i).getC(), triangles.get(i).getA());
            Edge cI =new Edge( triangles.get(i).getA(), triangles.get(i).getC());

            if (!result.contains(a) ){
                if (!result.contains(aI)){
                    result.add(a);
                }
            }
            if (!result.contains(b) ){
                if (!result.contains(bI)){
                    result.add(b);
                }
            }
            if (!result.contains(c) ){
                if (!result.contains(cI)){
                    result.add(c);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Triangle[" + a + ", " + b + ", " + c + "]";
    }
}