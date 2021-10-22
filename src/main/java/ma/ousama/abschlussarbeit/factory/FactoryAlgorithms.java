package ma.ousama.abschlussarbeit.factory;


import ma.ousama.abschlussarbeit.model.Graph;
import ma.ousama.abschlussarbeit.service.AbstractGraphColoring.AbstractAlgorithms;
import ma.ousama.abschlussarbeit.service.algorithms.RNG1;
import ma.ousama.abschlussarbeit.service.algorithms.RNG2;
import ma.ousama.abschlussarbeit.service.algorithms.RNG3;

import java.util.ArrayList;

/*
 * Factory generates objects of concrete class based on given information.
 */
public class FactoryAlgorithms {
//    private static int m;

    FactoryAlgorithms() {
    }

    public static ArrayList<AbstractAlgorithms> getAlgorithms(ArrayList<String> algorithms, Graph gr) {

        ArrayList<AbstractAlgorithms> algorithm = new ArrayList<>();

        for (String algorithm1 : algorithms) {
            if (algorithm1.contains("RNG1")) {
                algorithm.add(new RNG1(gr));
            }
            if (algorithm1.contains("RNG2")) {
                algorithm.add(new RNG2(gr));
            }
            if (algorithm1.equals("RNG3")) {
                algorithm.add(new RNG3(gr));
            }
        }
        return algorithm;
    }

}
