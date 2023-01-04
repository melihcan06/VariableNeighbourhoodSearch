package com.example.variableneighbourhoodsearch.services;

import com.example.variableneighbourhoodsearch.model.*;

import java.util.*;

public class VNSAlgorithm {

    //TODO
    public HashSet<String> shakingPhase(Graph subGraph, int k) {
        HashSet<String> c2 = new HashSet<>();
        for (int i = 0; i < k; i++) {
            if (Math.random() <= 0.5) {

            } else {

            }
        }


        return c2;
    }

    //TODO
    public Graph localSearch(Graph subGraph) {

        return subGraph;
    }

    public void run(Graph graph) {
        Graph subGraph = graph.generateInitialSolution();
        subGraph.printGraph();
        System.out.println(subGraph.isGraphConnected());
        subGraph.printLabels();//c

        for (int i = 0; i < 1; i++) {//TODO gecici olarak durma kosulu, degistir!
            int k = 1;
            int kMax = subGraph.getSetLabels().size() + subGraph.getSetLabels().size() / 3;
            while (k < kMax) {
                Graph subGraph2;
                do {
                    HashSet<String> c2 = shakingPhase(subGraph, k);
                    subGraph2 = subGraph.createSubGraph(c2);
                } while (subGraph2.isGraphConnected());
                do {
                    subGraph2 = localSearch(subGraph2);
                } while (subGraph2.isGraphConnected());
                if (subGraph2.getSetLabels().size() < subGraph2.getSetLabels().size()) {
                    subGraph = subGraph2;
                    k = 1;
                } else {
                    k++;
                }
            }
        }

    }
}
