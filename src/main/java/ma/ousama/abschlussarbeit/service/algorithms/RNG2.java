package ma.ousama.abschlussarbeit.service.algorithms;

import ma.ousama.abschlussarbeit.delaunayTriangulierung.DelaunayTriangulator;
import ma.ousama.abschlussarbeit.delaunayTriangulierung.NotEnoughPointsException;
import ma.ousama.abschlussarbeit.model.Edge;
import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.model.Node;
import ma.ousama.abschlussarbeit.model.Triangle;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.Algorithm;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class RNG2 extends AbstractAlgorithms {

    public RNG2(Graph gr) {
        super(gr);
    }

    @Override
    public Algorithm executeAlgorithm() throws NotEnoughPointsException {
		// Laufzeit
		double start = System.currentTimeMillis();
		double end = 0;

        // res
        ArrayList<Node> node = graph.getV();
        ArrayList<Edge> edge = new ArrayList<Edge>();

        // Step 1. Compute the Voronoi diagram of the set of points.
        // Step 2. Obtain the Delaunay triangulation from the Voronoi diagram.
        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(graph.getV());
        delaunayTriangulator.triangulate(); // DT apply
        List<Triangle> triangles = delaunayTriangulator.getTriangles();
        ArrayList<Edge> RNG = Triangle.convertTriangleToEdges(triangles);

        // Step 3. For each pair of points (pi,pj), associated with an edge of the DT,
        // compute dkmax = max {d(pk,pi),d(pk,pj ) for k = 1...n; k != i; k != j.
        ArrayList<Pair<Pair<Edge, Integer>, Double>> dkMax = new ArrayList<Pair<Pair<Edge, Integer>, Double>>();

        for (Edge pair : RNG) {
            for (int k = 0; k < node.size(); k++) {
                Node i = pair.getStart();
                Node j = pair.getEnd();
                Node nodeK = node.get(k);
                if (!pair.getStart().equals(nodeK) && !pair.getEnd().equals(nodeK)) {
                    dkMax.add(new Pair<>(new Pair<>(pair, k), Math.max(getDistance(i, nodeK), getDistance(j, nodeK))));
                }
            }
        }

        // Step 4. Same as Step 3 of algorithm RNG-1, with edges of the DT only
        boolean found;
        for (Edge pair : RNG) {
            found = false;
            Node i = pair.getStart();
            Node j = pair.getEnd();

            for (int l = 0; l < node.size() - 2; l++) {
                Double dkMaxValue = dkMax.get(0).getValue();
                double distanceValue = getDistance(i, j);

                if (dkMaxValue < distanceValue) {
                    found = true;
                }
                dkMax.remove(0);
            }
            if (found == false) {
                edge.add(new Edge(i, j));
            }
        }
		end = (System.currentTimeMillis() - start) / 1000;
		return new Algorithm("RNG2", edge.size(), new Graph(node, edge), end);
    }

    public double getDistance(Node start, Node end) { // start= (Xs,Ys) ende = (Xe,Ye)
        double x = Math.pow(Math.abs(end.getX() - start.getX()), 2);
        double y = Math.pow(Math.abs(end.getY() - start.getY()), 2);
        return Math.sqrt(x + y);
    }

}
