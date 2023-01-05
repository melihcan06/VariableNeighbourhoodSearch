package com.example.variableneighbourhoodsearch.services;

import com.example.variableneighbourhoodsearch.model.*;
import org.springframework.util.*;

import java.util.*;

public class VNSAlgorithm {

    public String addUnusedLabelToGraph(Graph graph, Graph subGraph) {
        String addedLabel = "";
        HashSet<String> setSubGraphLabels = subGraph.getSetLabels();
        List<String> listUnusedLabels = graph.getSetLabels().stream().filter(label -> !setSubGraphLabels.contains(label)).toList();
        if (!CollectionUtils.isEmpty(listUnusedLabels)) {
            Collections.shuffle(listUnusedLabels);
            addedLabel = listUnusedLabels.get(0);
            subGraph.getSetLabels().add(addedLabel);
            subGraph = graph.createSubGraph(subGraph.getSetLabels(), false);
        }
        return addedLabel;
    }

    public String deleteLabelFromSubGraph(Graph graph, Graph subGraph) {
        String deletedLabel = "";
        List<String> listUsedLabels = subGraph.getSetLabels().stream().toList();
        if (!CollectionUtils.isEmpty(listUsedLabels)) {
            Collections.shuffle(listUsedLabels);
            deletedLabel = listUsedLabels.get(0);
            subGraph.getSetLabels().remove(deletedLabel);
            subGraph = graph.createSubGraph(subGraph.getSetLabels(), false);
        }
        return deletedLabel;
    }

    public HashSet<String> shakingPhase(Graph graph, Graph subGraph, int k) {
        for (int i = 0; i < k; i++) {
            if (Math.random() <= 0.5 && subGraph.getSetLabels().size() > 2) {
                deleteLabelFromSubGraph(graph, subGraph);//subgraphlar degisiyor mu??
            } else {
                addUnusedLabelToGraph(graph, subGraph);//subgraphlar degisiyor mu??
            }
        }
        return subGraph.getSetLabels();
    }

    //TODO
    public Graph localSearch(Graph graph, Graph subGraph) {
        while (!subGraph.isGraphConnected()) {
            addUnusedLabelToGraph(graph, subGraph);
        }
        for (int i = 0; i < subGraph.getSetLabels().size(); i++) {
            String deletedLabel = deleteLabelFromSubGraph(graph, subGraph);
            if(!subGraph.isGraphConnected() && !deletedLabel.isBlank()){
                subGraph.getSetLabels().add(deletedLabel);
            }
        }
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
                    HashSet<String> c2 = shakingPhase(graph, subGraph, k);
                    subGraph2 = subGraph.createSubGraph(c2);
                } while (subGraph2.isGraphConnected());
                do {
                    subGraph2 = localSearch(graph, subGraph2);
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
