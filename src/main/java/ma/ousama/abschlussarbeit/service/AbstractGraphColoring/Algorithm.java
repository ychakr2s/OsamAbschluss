package ma.ousama.abschlussarbeit.service.AbstractGraphColoring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.model.GraphVariable;

public class Algorithm {
    /*
     * This Class contains these Attributes to describe the Algorithm, which have to be outputed in Json-Format
     */
    String algorithm;
    @JsonIgnore
    int edges;
    double time;
    Graph graph;


    GraphVariable grV;

    public Algorithm(String name, int num, Graph gr, double time) {
        this.algorithm = name;
        this.edges = num;
        this.graph = gr;
        this.time = time;
        this.grV = grV;
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getEdges() {
        return edges;
    }

    public void setEdges(int edges) {
        this.edges = edges;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
    public GraphVariable getGrV() {
        return grV;
    }

    public void setGrV(GraphVariable grV) {
        this.grV = grV;
    }

}






