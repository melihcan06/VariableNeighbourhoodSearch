package com.example.variableneighbourhoodsearch.services;

import com.example.variableneighbourhoodsearch.model.*;
import org.springframework.util.*;

import java.util.*;
import java.util.stream.*;

public class VNSAlgorithm {

    public String addUnusedLabelToGraph(HashSet<String> allLabels, HashSet<String> c2Labels) {
        String addedLabel = "";
        List<String> listUnusedLabels = allLabels.stream().filter(label -> !c2Labels.contains(label)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listUnusedLabels)) {
            Collections.shuffle(listUnusedLabels);
            addedLabel = listUnusedLabels.get(0);
            c2Labels.add(addedLabel);
        }
        return addedLabel;
    }

    public String deleteLabelFromSubGraph(HashSet<String> c2Labels) {
        String deletedLabel = "";
        List<String> listUsedLabels = new ArrayList<>(c2Labels);
        if (!CollectionUtils.isEmpty(listUsedLabels) && listUsedLabels.size() > 2) {
            Collections.shuffle(listUsedLabels);
            deletedLabel = listUsedLabels.get(0);
            c2Labels.remove(deletedLabel);
        }
        return deletedLabel;
    }

    //allLabels -> graphLabels; cLabels -> subGraphLabels, C(feasible sol.) labels in algorithm
    public HashSet<String> shakingPhase(HashSet<String> allLabels, HashSet<String> cLabels, int k) {
        HashSet<String> c2Labels = (HashSet<String>) cLabels.clone();
        System.out.println("shakingPhase baslangic");
        c2Labels.forEach(s1 -> System.out.println(s1));
        System.out.println("---");
        for (int i = 0; i < k; i++) {
            if (Math.random() <= 0.5 && c2Labels.size() > 2) {//islemi hizlandirmak icin 2. kontrol koyuldu
                deleteLabelFromSubGraph(c2Labels);
            } else {
                addUnusedLabelToGraph(allLabels, c2Labels);
            }
        }
        c2Labels.forEach(s1 -> System.out.println(s1));
        System.out.println("shakingPhase bitis");
        return c2Labels;
    }

    public Graph localSearch(Graph allGraph, Graph subGraph2) {
        System.out.println("localSearch baslangic");
        subGraph2.getLabels().forEach(s1 -> System.out.println(s1));
        System.out.println("---------------");
        while (!(subGraph2.isGraphConnected() && subGraph2.isAllNodesUsing(allGraph, subGraph2))) {
            //System.out.println("------BASLA-------");

            HashSet<String> c2 = subGraph2.getLabels();
            /*c2.forEach(s1 -> System.out.println(s1));
            System.out.println("---------------");*/

            addUnusedLabelToGraph(allGraph.getLabels(), c2);

            /*c2.forEach(s1 -> System.out.println(s1));
            System.out.println("---------------");*/

            subGraph2 = allGraph.createSubGraph(c2, false);

            /*System.out.println("---------------");
            subGraph2.getLabels().forEach(s1 -> System.out.println(s1));

            System.out.println("------BITIR------");*/
        }
        subGraph2.getLabels().forEach(s1 -> System.out.println(s1));
        System.out.println("---------------");
        for (int i = 0; i < subGraph2.getLabels().size(); i++) {
            HashSet<String> c2 = subGraph2.getLabels();
            String deletedLabel = deleteLabelFromSubGraph(c2);
            subGraph2 = allGraph.createSubGraph(c2, false);
            if (!(subGraph2.isGraphConnected() && subGraph2.isAllNodesUsing(allGraph, subGraph2)) && !deletedLabel.isBlank()) {
                c2.add(deletedLabel);
                subGraph2 = allGraph.createSubGraph(c2, false);
            }
        }
        subGraph2.getLabels().forEach(s1 -> System.out.println(s1));
        System.out.println("localSearch bitis");
        return subGraph2;
    }

    public Graph run(Graph graph) {
        Graph subGraph = graph.generateInitialSolution();
        System.out.println("Feasible Solution");
        subGraph.printGraphAll(graph);

        //for (int i = 0; i < 3; i++) {//durma kosulu ne olmali?
        do{
            int k = 1;
            int kMax = subGraph.getLabels().size() + subGraph.getLabels().size() / 3;
            while (k < kMax) {
                HashSet<String> c2 = shakingPhase(graph.getLabels(), subGraph.getLabels(), k);
                Graph subGraph2 = graph.createSubGraph(c2, false);
                subGraph2 = localSearch(graph, subGraph2);
                if (subGraph2.getLabels().size() < subGraph.getLabels().size()) {
                    subGraph = subGraph2.cloneGraph();
                    k = 1;
                } else {
                    k++;
                }
            }
            if(subGraph.getLabels().size()>2){
                //int breakp=0;
                //System.out.println("subGraph.getLabels().size()>2, i: "+i);
                System.out.println("subGraph.getLabels().size(): "+subGraph.getLabels().size());
                subGraph.printLabels();
            }
        } while(subGraph.getLabels().size()>2);//TODO grafımızın çözümüne uygun oldugu icin, baska problemde degistirilmelidir!!!
        System.out.println("Last Graph");
        subGraph.printGraphAll(graph);
        return subGraph;
    }
}
