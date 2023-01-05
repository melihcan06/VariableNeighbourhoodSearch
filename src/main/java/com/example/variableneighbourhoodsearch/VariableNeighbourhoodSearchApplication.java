package com.example.variableneighbourhoodsearch;

import com.example.variableneighbourhoodsearch.model.*;
import com.example.variableneighbourhoodsearch.services.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.*;

@SpringBootApplication
public class VariableNeighbourhoodSearchApplication {
    public static void trial1(){
        Graph graph = new Graph();
        Vertex vertex = new Vertex("1");
        LinkedList<Edge> listEdge = new LinkedList<>();
        Edge edge = new Edge();
        edge.setLabel("melo");
        listEdge.add(edge);
        graph.getGraph().put(vertex, null);

        Vertex vertex2 = new Vertex("1");
        LinkedList<Edge> listEdge2 = graph.getGraph().get(vertex2);
        Optional.ofNullable(listEdge2).stream().forEach(x->x.stream().forEach(y -> System.out.println(y.getLabel())));
        listEdge2.stream().forEach(x->x.getLabel());
        Optional.ofNullable(listEdge2).stream().flatMap(List::stream).forEach(y -> System.out.println(y.getLabel()));
    }

    public static void main(String[] args) {
        //SpringApplication.run(VariableNeighbourhoodSearchApplication.class, args);
        // TODO -> dfs spannig tree yi connected olarak gormek icin tum baglari yazmaliyiz
        Graph graph = new Graph();
        graph.readGraphFromText("C:\\Users\\melih\\Downloads\\VariableNeighbourhoodSearch\\src\\main\\graph.txt");
        graph.printGraph();
        System.out.println(graph.isGraphConnected());
        graph.printLabels();
        System.out.println("---------------");
        Graph subGraph = graph.generateInitialSolution();
        subGraph.printGraph();
        System.out.println(subGraph.isGraphConnected());
        subGraph.printLabels();
        VNSAlgorithm vns = new VNSAlgorithm();
        vns.run(graph);


    }
}