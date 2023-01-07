package com.example.variableneighbourhoodsearch.model;

import lombok.*;

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

    public HashSet<String> getLabels() {
        HashSet<String> labels = new HashSet<>();
        if (isGraphNotEmpty()) {
            graph.forEach((vertex, edges) -> {
                edges.forEach(edge -> {
                    labels.add(edge.getLabel());
                });
            });
        }
        return labels;
    }

    public Graph cloneGraph() {
        Graph newGraph = new Graph();
        if (isGraphNotEmpty()) {
            graph.forEach((vertex, edges) -> {
                Vertex vertex1 = new Vertex(vertex.getName());
                LinkedList<Edge> edges1 = new LinkedList<>();
                edges.forEach(edge -> {
                    Vertex vertex2 = new Vertex(edge.getVertex().getName());
                    Edge edge1 = new Edge(vertex2, edge.getLabel());
                    edges1.add(edge1);
                });
                newGraph.graph.put(vertex1, edges1);
            });
        }
        return newGraph;
    }

    public void printLabels() {
        if (getLabels() != null) {
            getLabels().forEach(System.out::println);
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

    public boolean isAllNodesUsing(Graph graphALl, Graph subGraph){
        return subGraph.graph.keySet().size() == graphALl.graph.keySet().size();
    }

    public void printGraphAll(){
        printGraph(true);
        System.out.println("Connected: " + isGraphConnected());
        printLabels();
    }

    public void printGraphAll(Graph allGraph){
        printGraph(true);
        System.out.println("Connected: " + isGraphConnected());
        System.out.println("All Nodes: " + isAllNodesUsing(allGraph, this));
        printLabels();
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
        if (setSubLabels != null && setSubLabels.size() > 0) {
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

    public Graph generateInitialSolution() {
        Graph subGraph = new Graph();//feasible solution?
        //Random rand = new Random();
        HashSet<String> setSubLabels = new HashSet<>();

        int sizeLabels;
        do {
            //sizeLabels = rand.nextInt(getLabels().size()) + 1;
            sizeLabels = getLabels().size() - 1;
            if (sizeLabels < getLabels().size()) {
                List<String> listSubLabels = new ArrayList<>(getLabels());
                Collections.shuffle(listSubLabels);
                setSubLabels = (HashSet<String>) listSubLabels.stream().limit(sizeLabels).collect(Collectors.toSet());
                subGraph = createSubGraph(setSubLabels, false);
            }
        } while (!(sizeLabels < getLabels().size() && subGraph.isGraphConnected() && isAllNodesUsing(this, subGraph)));//subGraph.graph.keySet().size() == graph.keySet().size()
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
                    getLabels().add(vertexAndLabel[1]);
                });
                graph.put(vertex, listEdgesOfVertex);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}