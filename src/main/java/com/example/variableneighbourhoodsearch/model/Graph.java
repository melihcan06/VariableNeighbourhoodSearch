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
    private HashSet<String> setLabels = new HashSet<>();

    public boolean isGraphNotEmpty() {
        if (graph != null && !graph.keySet().isEmpty()) {
            return true;
        }
        return false;
    }

    public void printLabels() {
        if (setLabels != null) {
            setLabels.stream().forEach(x -> System.out.println(x));
        }
    }

    public void printGraph(boolean showLabels) {
        if (isGraphNotEmpty()) {
            graph.forEach((key, value) -> {
                String s = "";
                if (showLabels) {
                    s = value.stream().map(Edge::toString).collect(Collectors.joining(Constants.edgesSeperator));
                } else {
                    s = value.stream().map(edge -> edge.getVertex().getName()).collect(Collectors.joining(Constants.edgesSeperator));
                }
                System.out.println(key.getName() + Constants.vertexAndEdgesSeperator + s);
            });
        }
    }

    public void printGraph() {
        printGraph(false);
    }

    // TODO -> dfs spannig tree yi connected olarak goremiyor!! duzelt!
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
            Stack<Vertex> stack = new Stack<>();
            Vertex firstVertex = graph.keySet().stream().findFirst().orElse(new Vertex());
            stack.push(firstVertex);
            graph.forEach((key, value) -> mapGraphCheck.put(key.getName(), Constants.DFSCheck.notChecked));
            HashMap<String, String> mapGraphCheckResult = dfs(mapGraphCheck, stack);
            return mapGraphCheckResult.values().stream()
                    .filter(Constants.DFSCheck.checked::equals)
                    .toList().size() == mapGraphCheck.size();
        }
        return false;
    }

    public Graph createSubGraph(HashSet<String> setSubLabels, boolean isConnectedControl) {
        Graph subGraph = new Graph();
        if (setSubLabels != null && setSubLabels.size() > 1) {
            graph.forEach((vertex, edges) -> {
                edges.forEach(edge -> {
                    if (setSubLabels.contains(edge.getLabel())) {
                        LinkedList<Edge> listEdges = Optional.ofNullable(subGraph.graph.get(vertex)).orElse(new LinkedList<>());
                        listEdges.add(edge);
                        subGraph.graph.put(vertex, listEdges);
                        Vertex vertexSecond = new Vertex(edge.getVertex().getName());
                        LinkedList<Edge> listEdgesOfVertexSecond = Optional.ofNullable(subGraph.graph.get(vertexSecond)).orElse(new LinkedList<>());
                        subGraph.graph.putIfAbsent(vertexSecond, listEdgesOfVertexSecond);
                    }
                });
            });
            if (isConnectedControl && !subGraph.isGraphConnected()) {
                return new Graph();
            }
        }
        return subGraph;
    }

    public Graph createSubGraph(HashSet<String> setSubLabels) {
        return createSubGraph(setSubLabels, true);
    }

    public Graph generateInitialSolution() {
        Graph subGraph = new Graph();//feasible solution?
        Random rand = new Random();
        HashSet<String> setSubLabels = new HashSet<>();

        int sizeLabels;
        do {
            sizeLabels = rand.nextInt(setLabels.size()) + 1;
            if (sizeLabels < setLabels.size()) {
                List<String> listSubLabels = new ArrayList<>(setLabels);
                Collections.shuffle(listSubLabels);
                setSubLabels = (HashSet<String>) listSubLabels.stream().limit(sizeLabels).collect(Collectors.toSet());
                subGraph = createSubGraph(setSubLabels, false);
            }
        } while (!(sizeLabels < setLabels.size() && subGraph.isGraphConnected() && subGraph.graph.keySet().size() == graph.keySet().size()));
        subGraph.setSetLabels(setSubLabels);
        return subGraph;
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
                    setLabels.add(vertexAndLabel[1]);
                });
                graph.put(vertex, listEdgesOfVertex);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}