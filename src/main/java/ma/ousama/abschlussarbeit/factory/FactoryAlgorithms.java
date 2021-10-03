package ma.ousama.abschlussarbeit.factory;



import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.effizient_Algorithm.EffizienteAlgorithm;
import ma.ousama.abschlussarbeit.service.naive_Algorithms.NaiveAlgorithm1;
import ma.ousama.abschlussarbeit.service.naive_Algorithms.NaiveAlgorithm2;

import java.util.ArrayList;

/*
 * Factory generates objects of concrete class based on given information.
 */
public class FactoryAlgorithms {
    private static int m;

    FactoryAlgorithms() {
    }

    public static ArrayList<AbstractAlgorithms> getAlgorithms(ArrayList<String> algorithms, Graph gr) {

        ArrayList<AbstractAlgorithms> algorithm = new ArrayList<>();
        String num = algorithms.get(algorithms.size() - 1);

        for (String algorithm1 : algorithms) {
            if (algorithm1.contains("NaiveAlgorithmus1")) {
                algorithm.add(new NaiveAlgorithm1(gr));
            }
            if (algorithm1.contains("NaiveAlgorithmus2")) {
                algorithm.add(new NaiveAlgorithm2(gr));
            }
            if (algorithm1.equals("EffizienterAlgorithmus")) {
                algorithm.add(new EffizienteAlgorithm(gr));
            }
        }
        return algorithm;
    }

}
