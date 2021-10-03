package ma.ousama.abschlussarbeit.service.naive_Algorithms;

import javafx.util.Pair;
import ma.ousama.abschlussarbeit.delaunayTriangulierung.NotEnoughPointsException;
import ma.ousama.abschlussarbeit.model.Edge;
import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.model.Node;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.Algorithm;
//import org.apache.commons.math3.util.Pair;

import java.util.LinkedList;

public class NaiveAlgorithm2 extends AbstractAlgorithms {

    public NaiveAlgorithm2(Graph gr) {
        super(gr);
    }

    public double getDistance(Node start, Node end) { // start= (Xs,Ys) ende = (Xe,Ye)
        double x = Math.pow(Math.abs(end.getX() - start.getX()), 2);
        double y = Math.pow(Math.abs(end.getY() - start.getY()), 2);
        return Math.sqrt(x + y);
    }

	// n^3
    @Override
    public Algorithm executeAlgorithm() throws NotEnoughPointsException {
        double start = System.currentTimeMillis();
        double end = 0;
        // Step 1: Compute the distance between all pairs of points d(pi,pj) i,j= 1,...,n, i!=j
        LinkedList<Node> node = graph.getV();
        LinkedList<Edge> edge = new LinkedList<Edge>();

        LinkedList<LinkedList<Double>> distance = new LinkedList<LinkedList<Double>>();

        for (int i = 0; i < node.size(); i++) {
            LinkedList<Double> tmp = new LinkedList<Double>();
            for (int j = 0; j < node.size(); j++) {
                if (i != j) {
                    tmp.add(getDistance(node.get(i), node.get(j)));
                } else {
                    tmp.add(null);
                }
            }
            distance.add(tmp);
        }

        //Step 2: For each pair of points (pi,pj) compute dkMax = max{ d(pk,pi),d(pk,pj)} for k =1,...,n k!=i, k!=j.
        // ( ( (p,q), k) , value)
        LinkedList<Pair<Pair<Pair<Integer, Integer>, Integer>, Double>> dkMax = new LinkedList<Pair<Pair<Pair<Integer, Integer>, Integer>, Double>>();

        for (int i = 0; i < distance.size(); i++) {
            for (int j = i + 1; j < distance.size(); j++) {
                for (int k = 0; k < node.size(); k++) {
                    if (k != i && k != j) {
                        dkMax.add(new Pair<>(new Pair<>(new Pair<>(i, j), k), Math
                                .max(getDistance(node.get(i), node.get(k)), getDistance(node.get(j), node.get(k)))));
                    }
                }
            }
        }

        //Step 3: For each pair of points search for a value of dkMax that is smaller than d(pi,pj). If such a points is not found, am edge is formed between pi and pj.

        boolean found;
        for (int i = 0; i < distance.size(); i++) {
            for (int j = i + 1; j < distance.size(); j++) {
                found = false;
                for (int l = 0; l < node.size() - 2; l++) {
                    if (dkMax.getFirst().getValue() < distance.get(i).get(j)) {
                        found = true;
                    }
                    dkMax.removeFirst();
                }
                if (found == false) {
                    edge.add(new Edge(node.get(i), node.get(j)));
                }
            }
        }
        end = (System.currentTimeMillis() - start) / 1000;

        return new Algorithm("Naive Algorithm 2", edge.size(), new Graph(node, edge), end);
    }

    @Override
    public void description() {

    }

    @Override
    public void printSolution() {

    }
}
