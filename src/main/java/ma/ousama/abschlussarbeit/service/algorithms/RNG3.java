package ma.ousama.abschlussarbeit.service.algorithms;

import ma.ousama.abschlussarbeit.delaunayTriangulierung.DelaunayTriangulator;
import ma.ousama.abschlussarbeit.delaunayTriangulierung.NotEnoughPointsException;
import ma.ousama.abschlussarbeit.model.*;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RNG3 extends AbstractAlgorithms {

    public RNG3(Graph gr) {
        super(gr);
    }

    @Override
    public Algorithm executeAlgorithm() throws NotEnoughPointsException {
        // Laufzeit
        double start = System.currentTimeMillis();
        double end = 0;

        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(graph.getV());
        delaunayTriangulator.triangulate(); // DT apply
        List<Triangle> triangles = delaunayTriangulator.getTriangles();

        // E:=DT
        ArrayList<Edge> RNG = Triangle.convertTriangleToEdges(triangles);

        // 6 Scans
        ArrayList<Node> rotated; // Liste der transformierten Knoten
        for (int k = 0; k <= 5; k++) {
            int angle = 60 * k;
            if (angle == 0) {
                rotated = graph.getV();
            } else {
                rotated = rotate(graph.getV(), angle);
                RNG = rotateEdge(RNG, angle);
            }

            @SuppressWarnings("unchecked")
            ArrayList<Edge> tmpRNG = (ArrayList<Edge>) RNG.clone(); // ConcurrentModificationException

            // Build an empty tree
            AVLTree<Node> avlTree = new AVLTree<Node>();

            // sort the points of V from right to left
            ArrayList<Node> sorted = sortByXCoordinate(rotated);

            Node vergleichKeinVorgaenger = null;

            for (int i = 0; i < graph.getV().size(); i++) {
                // insert pi, into T
                Node pi = sorted.get(i);

                try {
                    avlTree.insert(pi);
                } catch (Exception e) {
                    continue;
                }

                // for each edge pi,pj E DT such that j<i do
                for (Edge e : tmpRNG) {

                    // Finde die Kante, die mit dem Knoten pi startet
                    // Vergleiche x-Koordinate von Pi mit der x-Koordinate des Startknotens der
                    // Kante e in tmpRNG
                    boolean xCompare;
                    if (pi.getX_transformed() == e.getE_transformed().getStart().getX_transformed()) {
                        xCompare = true;
                    } else {
                        xCompare = false;
                    }

                    // Vergleiche y-Koordinate von Pi mit der y-Koordinate des Startknotens der
                    // Kante e in tmpRNG
                    boolean yCompare;
                    if (pi.getY_transformed() == e.getE_transformed().getStart().getY_transformed()) {
                        yCompare = true;
                    } else {
                        yCompare = false;
                    }

                    // Wenn xCompare und yCompare true sind, dann ist pi der Startknoten der Kante e
                    if (xCompare && yCompare) {
                        // Knoten pj ist der Endknoten der Kante e
                        Node pj = new Node(e.getEnd().getX(), e.getEnd().getY(),
                                e.getE_transformed().getEnd().getX_transformed(),
                                e.getE_transformed().getEnd().getY_transformed());
                        // Index der Knotens pj in der sortierten Knotenmenge
                        int j = sorted.indexOf(pj);

                        // bei der Index j kleiner als i ist
                        if (j < i) {
                            // pj = sorted.get(j);
                            Node v;
                            if (pj.getY_transformed() > pi.getY_transformed()) {
                                v = avlTree.naechste(pi);
                            } else {
                                v = avlTree.vorgaenger(pi);
                            }

                            if (v != null) {
                                while ((pi.getY_transformed() <= v.getY_transformed()
                                        && v.getY_transformed() < pj.getY_transformed())
                                        || (pj.getY_transformed() < v.getY_transformed()
                                        && v.getY_transformed() <= pi.getY_transformed())) {

                                    // if v E lun(pi, pj)
                                    if (lun(sorted, pi, pj).contains(v)) {

                                        RNG.remove(e); // ConcurrentModificationException ---> Problem: remove from the
                                        // same RNG-List
                                        break;
                                    } else {

                                        if (pj.getY_transformed() > pi.getY_transformed()) {
                                            try {

                                                v = avlTree.naechste(v);
                                            } catch (Exception e1) {
                                                break;
                                            }

                                        } else {
                                            try {
                                                if (!vergleichKeinVorgaenger.equals(v)) {
                                                    v = avlTree.vorgaenger(v);
                                                    vergleichKeinVorgaenger = v;
                                                } else {
                                                    v = avlTree.naechste(v);
                                                    vergleichKeinVorgaenger = v;
                                                }
                                            } catch (Exception e2) {
                                                break;
                                            }
                                        }
                                        avlTree.remove(v);
                                    }
                                }
                            }
                        }
                    }
                }
            } // for i:= 0 to n (through all the nodes)
        } // for k:= 0 to 5 (six scans)


        end = (System.currentTimeMillis() - start) / 1000;
        return new Algorithm("RNG3", RNG.size(), new Graph(graph.getV(), RNG), end);// here E = RNG ---> outG(V, E)
    }

    public ArrayList<Node> rotate(ArrayList<Node> nodes, int angle) {

        ArrayList<Node> res = new ArrayList<Node>();

        for (int i = 0; i < nodes.size(); i++) {
            double x = nodes.get(i).getX();
            double y = nodes.get(i).getY();

            double x_new = (x * Math.cos(Math.toRadians(angle))) - (y * Math.sin(Math.toRadians(angle)));
            double y_new = (x * Math.sin(Math.toRadians(angle))) + (y * Math.cos(Math.toRadians(angle)));


            res.add(new Node(x, y, x_new, y_new));
        }

        return res;
    }

    public ArrayList<Edge> rotateEdge(ArrayList<Edge> edges, int angle) {

        ArrayList<Edge> res = new ArrayList<Edge>();

        for (int i = 0; i < edges.size(); i++) {

            Edge e = edges.get(i);

            Node startKnoten_original = e.getStart();
            Node endeKnoten_original = e.getEnd();

            double x_start = startKnoten_original.getX();
            double y_start = startKnoten_original.getY();

            double x_startKnoten_transformed = (x_start * Math.cos(Math.toRadians(angle)))
                    - (y_start * Math.sin(Math.toRadians(angle)));
            double y_startKnoten_transformed = (x_start * Math.sin(Math.toRadians(angle)))
                    + (y_start * Math.cos(Math.toRadians(angle)));


            double x_end = endeKnoten_original.getX();
            double y_end = endeKnoten_original.getY();

            double x_endKnoten_transformed = (x_end * Math.cos(Math.toRadians(angle)))
                    - (y_end * Math.sin(Math.toRadians(angle)));
            double y_endKnoten_transformed = (x_end * Math.sin(Math.toRadians(angle)))
                    + (y_end * Math.cos(Math.toRadians(angle)));


            Edge edge_transformed = new Edge(new Node(x_startKnoten_transformed, y_startKnoten_transformed),
                    new Node(x_endKnoten_transformed, y_endKnoten_transformed));

            // new Edge(start, ende, Edge_transf)
            res.add(new Edge(startKnoten_original, endeKnoten_original, edge_transformed));
        }
        return res;
    }

    public ArrayList<Node> sortByXCoordinate(ArrayList<Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                double x1 = n1.getX_transformed();
                double x2 = n2.getX_transformed();

                double y1 = n1.getY_transformed();
                double y2 = n2.getY_transformed();

                if (x1 < x2) {
                    return -1;
                } else if (x1 == x2) {
                    if (y1 < y2) {
                        return -1;
                    } else if (y1 == y2) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
        return nodes;
    }

    public ArrayList<Node> lun(ArrayList<Node> n, Node p, Node q) {

        @SuppressWarnings("unchecked")
        ArrayList<Node> nodes = (ArrayList<Node>) n.clone();
        ArrayList<Node> lun = new ArrayList<Node>();

        if (nodes.contains(p)) {
            nodes.remove(p);
        }
        if (nodes.contains(q)) {
            nodes.remove(q);
        }

        for (Node z : nodes) {

            if (Edge.getDistance(p, z) < Edge.getDistance(p, q) && Edge.getDistance(q, z) < Edge.getDistance(p, q)) {
                lun.add(z);
            }
        }
        return lun;
    }

}