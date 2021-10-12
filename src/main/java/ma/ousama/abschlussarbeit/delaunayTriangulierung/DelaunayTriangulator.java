package ma.ousama.abschlussarbeit.delaunayTriangulierung;

import ma.ousama.abschlussarbeit.model.Edge;
import ma.ousama.abschlussarbeit.model.Node;
import ma.ousama.abschlussarbeit.model.Triangle;

import java.util.List;

public class DelaunayTriangulator {

    private List<Node> pointSet;
    private TriangleSoup triangleSoup;

    public DelaunayTriangulator(List<Node> pointSet) {
        this.pointSet = pointSet;
        this.triangleSoup = new TriangleSoup();
    }

    public void triangulate() throws NotEnoughPointsException {
        triangleSoup = new TriangleSoup();

        if (pointSet == null || pointSet.size() < 3) {
            throw new NotEnoughPointsException("Less than three points in point set.");
        }

        double maxOfAnyCoordinate = 0.0d;

        for (Node node : getPointSet()) {
            maxOfAnyCoordinate = Math.max(Math.max(node.getX(), node.getY()), maxOfAnyCoordinate);
        }

        maxOfAnyCoordinate *= 16.0d;

        Node p1 = new Node(0.0d, 3.0d * maxOfAnyCoordinate);
        Node p2 = new Node(3.0d * maxOfAnyCoordinate, 0.0d);
        Node p3 = new Node(-3.0d * maxOfAnyCoordinate, -3.0d * maxOfAnyCoordinate);

        Triangle superTriangle = new Triangle(p1, p2, p3);

        triangleSoup.add(superTriangle);

        for (int i = 0; i < pointSet.size(); i++) {
            Triangle triangle = triangleSoup.findContainingTriangle(pointSet.get(i));

            if (triangle == null) {

                Edge edge = triangleSoup.findNearestEdge(pointSet.get(i));

                Triangle first = triangleSoup.findOneTriangleSharing(edge);
                Triangle second = triangleSoup.findNeighbour(first, edge);

                Node firstNoneEdgeVertex = first.getNoneEdgeVertex(edge);
                Node secondNoneEdgeVertex = second.getNoneEdgeVertex(edge);

                triangleSoup.remove(first);
                triangleSoup.remove(second);

                Triangle triangle1 = new Triangle(edge.getStart(), firstNoneEdgeVertex, pointSet.get(i));
                Triangle triangle2 = new Triangle(edge.getEnd(), firstNoneEdgeVertex, pointSet.get(i));
                Triangle triangle3 = new Triangle(edge.getStart(), secondNoneEdgeVertex, pointSet.get(i));
                Triangle triangle4 = new Triangle(edge.getEnd(), secondNoneEdgeVertex, pointSet.get(i));

                triangleSoup.add(triangle1);
                triangleSoup.add(triangle2);
                triangleSoup.add(triangle3);
                triangleSoup.add(triangle4);

                legalizeEdge(triangle1, new Edge(edge.getStart(), firstNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle2, new Edge(edge.getEnd(), firstNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle3, new Edge(edge.getStart(), secondNoneEdgeVertex), pointSet.get(i));
                legalizeEdge(triangle4, new Edge(edge.getEnd(), secondNoneEdgeVertex), pointSet.get(i));
            } else {

                Node a = triangle.getA();
                Node b = triangle.getB();
                Node c = triangle.getC();

                triangleSoup.remove(triangle);

                Triangle first = new Triangle(a, b, pointSet.get(i));
                Triangle second = new Triangle(b, c, pointSet.get(i));
                Triangle third = new Triangle(c, a, pointSet.get(i));

                triangleSoup.add(first);
                triangleSoup.add(second);
                triangleSoup.add(third);

                legalizeEdge(first, new Edge(a, b), pointSet.get(i));
                legalizeEdge(second, new Edge(b, c), pointSet.get(i));
                legalizeEdge(third, new Edge(c, a), pointSet.get(i));
            }
        }

        triangleSoup.removeTrianglesUsing(superTriangle.getA());
        triangleSoup.removeTrianglesUsing(superTriangle.getB());
        triangleSoup.removeTrianglesUsing(superTriangle.getC());
    }

    private void legalizeEdge(Triangle triangle, Edge edge, Node newVertex) {
        Triangle neighbourTriangle = triangleSoup.findNeighbour(triangle, edge);


        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInCircumcircle(newVertex)) {
                triangleSoup.remove(triangle);
                triangleSoup.remove(neighbourTriangle);

                Node noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge);

                Triangle firstTriangle = new Triangle(noneEdgeVertex, edge.getStart(), newVertex);
                Triangle secondTriangle = new Triangle(noneEdgeVertex, edge.getEnd(), newVertex);

                triangleSoup.add(firstTriangle);
                triangleSoup.add(secondTriangle);

                legalizeEdge(firstTriangle, new Edge(noneEdgeVertex, edge.getStart()), newVertex);
                legalizeEdge(secondTriangle, new Edge(noneEdgeVertex, edge.getEnd()), newVertex);
            }
        }
    }

    public List<Node> getPointSet() {
        return pointSet;
    }

    public List<Triangle> getTriangles() {
        return triangleSoup.getTriangles();
    }

}