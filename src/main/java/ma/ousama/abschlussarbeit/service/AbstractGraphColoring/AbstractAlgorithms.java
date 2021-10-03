package ma.ousama.abschlussarbeit.service.AbstractGraphColoring;



import ma.ousama.abschlussarbeit.delaunayTriangulierung.NotEnoughPointsException;
import ma.ousama.abschlussarbeit.model.Graph;

import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractAlgorithms {
    public Graph graph;
//    protected int V;

    protected AbstractAlgorithms(Graph gr) {
        this.graph = gr;
        // No. of vertices
//        this.V = gr.getNumVertices();
    }

    /*
     * The first extended Algorithms must be executed.
     * The seconds method describes the implemented Algorithm.
     * The third method prints the test of the Algorithms.
     */
    public abstract Algorithm executeAlgorithm() throws NotEnoughPointsException;

    public abstract void description();

    public abstract void printSolution();

    protected void setColor(int vertex, int color, int[] resultColors) {
        resultColors[vertex] = color;
    }

    protected int getColor(int vertex, int[] resultColor) {
        return resultColor[vertex];
    }

    /*
     * This method returns the Vertex with the highest adjacency degree
     */
//    public int vertexHighstAdjDegree(int[] ver) {
//        int max = 0;
//        int vertex = 0;
//        int count = 0;
//        if (ver.length > 0) {
//            for (int i1 : ver) {
//                if (graph.getVertexDegree(i1) > max) {
//                    max = graph.getVertexDegree(i1);
//                    vertex = i1;
//                }
//
//                if (graph.getVertexDegree(i1) == 0 && max == 0 && count == 0) {
//                    vertex = i1;
//                    count++;
//                }
//            }
//            return vertex;
//        } else
//            return -1;
//    }

    // remove an element from Array
    protected int[] remove(int[] arr, int v) {
        int[] ret = new int[arr.length - 1];
        if (arr.length > 1) {
            int cout = 0;
            for (int i1 : arr) {
                if (i1 != v) {
                    ret[cout] = i1;
                    cout++;
                }
            }
            return ret;
        } else
            return ret;
    }

//    protected int findRightColor(Graph graph, int cv, int[] resultColors, boolean[] available) {
//        // Process all adjacent vertices and flag their colors as unavailable
//        for (int i : graph.getEdges(cv)) {
//            if (getColor(i, resultColors) != -1) {
//                available[getColor(i, resultColors)] = false;
//            }
//        }
//
//        // Find the first available color
//        int cr;
//        for (cr = 0; cr < graph.getNumVertices(); cr++) {
//            if (available[cr])
//                break;
//        }
//        return cr;
//    }

    private boolean colorIsUsed(int d, int[] a, int length) {
        if (d == -1)
            return false;
        if (length == 0 && a[length] != -1) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (d == a[i])
                return false;
        }
        return true;
    }

    protected int computeResultsColors(int[] resultColor) {
        int result = 0;
        for (int i = 0; i < resultColor.length; i++) {
            if (colorIsUsed(getColor(i, resultColor), resultColor, i)) {
                result++;
            }
        }
        return result;
    }

    /*
     * this method determine the colors which is used to color this Graph
     */
    protected int[] usedColor(int[] resultColor) {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < resultColor.length; i++) {
            if (colorIsUsed(getColor(i, resultColor), resultColor, i)) {
                colors.add(getColor(i, resultColor));
            }
        }
        Collections.sort(colors);
        return colors.stream().mapToInt(i -> i).toArray();
    }

    private boolean test(int[] resultColors, Graph graph) {
        for (int i = 0; i < resultColors.length; i++) {
            if (resultColors[i] == -1)
                return false;

//            for (int v : graph.getEdges(i)) {
//                if (graph.isEdges(i, v) && resultColors[i] == resultColors[v]) {
//                    return false;
//                }
//            }
        }
        return true;
    }

    /*
     * This method shows how many colors we need it to color the Graph
     */
    private String toString(int[] resultColor) {
        String print = "";
        print += "We need " + computeResultsColors(resultColor) + " Colors to Color this Graph\n";
        return print;
    }

    protected void printTest(int[] resultColors, Graph graph) {
        if (test(resultColors, graph))
            System.out.println("+++++++++++++++++++ the Algorithm runs correctly +++++++++++++++++++++");
        else
            System.out.println("+++++++++++++++++++ the Algorithm runs wrongly +++++++++++++++++++++++");
        System.out.println(toString(resultColors));
    }
}
