package ma.ousama.abschlussarbeit.model;

import java.util.ArrayList;

/**
 * @author Ousama
 * @version 1.0
 */

public class Graph {

    private ArrayList<Node> v;
    private ArrayList<Edge> e;

    public Graph(Graph g) {
        this.v = g.getV();
        this.e = g.getE();
    }

    public Graph(ArrayList<Node> v, ArrayList<Edge> e) {
        this.v = v;
        this.e = e;
    }

    public ArrayList<Node> getV() {
        return v;
    }

    public ArrayList<Edge> getE() {
        return e;
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