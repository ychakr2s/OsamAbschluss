package ma.ousama.abschlussarbeit.model;

import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.Algorithm;

import java.util.ArrayList;
/*
 * In order to Output the result in Json-Format, i this class contains a Graph Object and algorithms-Objects
 */

public class JsonOutput {
    //    private Graph graph;
    private ArrayList<Algorithm> algorithms;

    public JsonOutput(ArrayList<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

}
