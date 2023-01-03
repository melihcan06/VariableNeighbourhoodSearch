package com.example.variableneighbourhoodsearch.services;

import com.example.variableneighbourhoodsearch.model.*;

public class VNSAlgorithm {
    public void run(Graph graph){
        Graph subGraph = graph.generateInitialSolution();
        subGraph.printGraph();
        System.out.println(subGraph.isGraphConnected());
        subGraph.printLabels();
    }
}
