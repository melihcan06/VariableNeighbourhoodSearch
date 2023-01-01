package com.example.variableneighbourhoodsearch.model;

import lombok.*;
import org.springframework.util.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Graph {

    private HashMap<Vertex, LinkedList<Edge>> graph = new HashMap<>();

    public boolean isGraphNotEmpty() {
        if (graph != null && !graph.keySet().isEmpty()) {
            return true;
        }
        return false;
    }

    public void printGraph() {
        printGraph(false);
    }

    public void printGraph(boolean showLabels){
        if (isGraphNotEmpty()) {
            graph.forEach((key, value) -> {
                String s = "";
                if(showLabels) {
                    s = value.stream().map(Edge::toString).collect(Collectors.joining(Constants.edgesSeperator));
                } else {
                    s = value.stream().map(edge -> edge.getVertex().getName()).collect(Collectors.joining(Constants.edgesSeperator));
                }
                System.out.println(key.getName() + Constants.vertexAndEdgesSeperator + s);
            });
        }
    }

    private HashMap<String, String> dfs(HashMap<String, String> mapGraphCheck, Stack<Vertex> stack) {
        while (stack != null && !stack.isEmpty()) {
            Vertex vertex = stack.pop();
            LinkedList<Edge> listEdges = graph.get(vertex);
            if (Constants.DFSCheck.notChecked.equals(mapGraphCheck.get(vertex.getName()))) {
                mapGraphCheck.put(vertex.getName(), Constants.DFSCheck.checking);
                for (Edge edge : listEdges) {
                    if (Constants.DFSCheck.notChecked.equals(mapGraphCheck.get(edge.getVertex().getName()))) {
                        stack.push(edge.getVertex());
                    }
                }
                mapGraphCheck = dfs(mapGraphCheck, stack);
                mapGraphCheck.put(vertex.getName(), Constants.DFSCheck.checked);
            }
        }
        return mapGraphCheck;
    }

    public boolean isGraphConnected() {
        if (isGraphNotEmpty()) {
            HashMap<String, String> mapGraphCheck = new HashMap<>(); // vertex.name, Constants.DFSCheck
            graph.forEach((key, value) -> mapGraphCheck.put(key.getName(), Constants.DFSCheck.notChecked));
            Vertex firstVertex = graph.keySet().stream().findFirst().orElse(new Vertex());
            Stack<Vertex> stack = new Stack<>();
            stack.push(firstVertex);
            HashMap<String, String> mapGraphCheckResult = dfs(mapGraphCheck, stack);
            return mapGraphCheckResult.values().stream()
                    .filter(Constants.DFSCheck.checked::equals)
                    .toList().size() == mapGraphCheck.size();
        }
        return false;
    }

    // TODO -> yazilacak!
    public boolean isGraphValid() {
        if (isGraphConnected()) {
            return true;
        }
        return false;
    }

    public void readGraphFromText(String path) {
        try {
            File graphFile = new File(path);
            Scanner reader = new Scanner(graphFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                //vakit kaybetmemek adina null kontrolleri vs yapmiyorum, verinin dogru geldiginden emin oluyorum

                String[] vertexAndEdges = line.split(Constants.vertexAndEdgesSeperator);
                String[] edges = vertexAndEdges[1].split(Constants.edgesSeperator);
                Vertex vertex = new Vertex(vertexAndEdges[0]);
                LinkedList<Edge> listEdgesOfVertex = Optional.ofNullable(graph.get(vertex)).orElse(new LinkedList<>());
                Arrays.stream(edges).forEach(x -> {
                    String[] vertexAndLabel = x.split(Constants.edgeSeperator);
                    Vertex vertexSecond = new Vertex(vertexAndLabel[0]);
                    LinkedList<Edge> listEdgesOfVertexSecond = Optional.ofNullable(graph.get(vertexSecond)).orElse(new LinkedList<>());
                    graph.putIfAbsent(vertexSecond, listEdgesOfVertexSecond);
                    listEdgesOfVertex.add(new Edge(vertexSecond, vertexAndLabel[1]));
                });
                graph.put(vertex, listEdgesOfVertex);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}