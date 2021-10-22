package ma.ousama.abschlussarbeit.model;

import ma.ousama.abschlussarbeit.delaunayTriangulierung.NotEnoughPointsException;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.Algorithm;

import java.util.ArrayList;

/*
 * The Context class doesn't implement an algorithm directly.
 * Instead, Context refers to the Strategy interface for performing an algorithm (algorithm.executeGraphAlgorithm()),
 * which makes Context independent of how an algorithm is implemented.
 */
public class Context {

    private ArrayList<AbstractAlgorithms> algorithms;

    public Context(ArrayList<AbstractAlgorithms> algorithms) {
        this.algorithms = algorithms;
    }

    public ArrayList<Algorithm> execute() throws NotEnoughPointsException {
        ArrayList<Algorithm> alg = new ArrayList<>();
        //delegates behavior to Strategy object
        for (AbstractAlgorithms algorithm : algorithms) {
            alg.add(algorithm.executeAlgorithm());
        }
        return alg;
    }
}