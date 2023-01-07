package com.example.variableneighbourhoodsearch.services;

import com.example.variableneighbourhoodsearch.model.*;

import java.util.*;
import java.util.stream.*;

public class PrimAlgorithm {
    // Define the count of vertices available in the graph
    private int countOfVertices = 9;
    private HashMap<String, Integer> mapVertexNameAndId = new HashMap<String, Integer>();

    public int getCountOfVertices() {
        return countOfVertices;
    }

    public void setCountOfVertices(int countOfVertices) {
        this.countOfVertices = countOfVertices;
    }

    public HashMap<String, Integer> getMapVertexNameAndId() {
        return mapVertexNameAndId;
    }

    public void setMapVertexNameAndId(HashMap<String, Integer> mapVertexNameAndId) {
        this.mapVertexNameAndId = mapVertexNameAndId;
    }

    // create findMinKeyVertex() method for finding the vertex v that has minimum key-value and that is not added MST yet
    int findMinKeyVertex(int keys[], Boolean setOfMST[]) {
        // Initialize min value and its index
        int minimum_index = -1;
        int minimum_value = Integer.MAX_VALUE;

        //iterate over all vertices to find minimum key-value vertex
        for (int vertex = 0; vertex < countOfVertices; vertex++)
            if (setOfMST[vertex] == false && keys[vertex] < minimum_value) {
                minimum_value = keys[vertex];
                minimum_index = vertex;
            }

        return minimum_index;
    }

    private String getKeyByValue(Integer integer) {
        for (Map.Entry<String, Integer> map : mapVertexNameAndId.entrySet()) {
            if (Objects.equals(map.getValue(), integer)) {
                return map.getKey();
            }
        }
        return "";
    }

    // create showMinimumSpanningTree for printing the constructed MST stored in mstArray[]
    void showMinimumSpanningTree(int mstArray[], int graphArray[][]) {
        if (mapVertexNameAndId.size() > 0) {
            System.out.println("Edges");
            for (int j = 1; j < countOfVertices; j++) {
                System.out.println(getKeyByValue(mstArray[j]) + " <-> " + getKeyByValue(j) + "\t \t");
            }
        } else {
            System.out.println("Edge \t\t Weight");
            for (int j = 1; j < countOfVertices; j++) {
                System.out.println(mstArray[j] + " <-> " + j + "\t \t" + graphArray[j][mstArray[j]]);
            }
        }
    }

    // create designMST() method for constructing and printing the MST. The graphArray[][] is an adjacency matrix that defines the graph for MST.
    public void designMST(int graphArray[][]) {
        // create array of size total number of vertices, i.e., countOfVertices for storing the MST
        int mstArray[] = new int[countOfVertices];

        // create keys[] array for selecting an edge having minimum weight in cut
        int keys[] = new int[countOfVertices];

        // create setOfMST array of type boolean for representing the set of vertices included in MST
        Boolean setOfMST[] = new Boolean[countOfVertices];

        // set the value of the keys to infinite
        for (int j = 0; j < countOfVertices; j++) {
            keys[j] = Integer.MAX_VALUE;
            setOfMST[j] = false;
        }

        // set value 0 to the 1st vertex because first vertes always include in MST.
        keys[0] = 0; // it select as first vertex
        mstArray[0] = -1; // set first value of mstArray to -1 to make it root of MST

        // The vertices in the MST will be equal to the countOfVertices
        for (int i = 0; i < countOfVertices - 1; i++) {
            // select the vertex having minimum key and that is not added in the MST yet from the set of vertices
            int edge = findMinKeyVertex(keys, setOfMST);

            // Add the selected minimum key vertex to the setOfMST
            setOfMST[edge] = true;

            // change the key value and the parent index of all the adjacent vertices of the selected vertex. The vertices that are not yet included in Minimum Spanning Tree are only considered.
            for (int vertex = 0; vertex < countOfVertices; vertex++)

                // The value of the graphArray[edge][vertex] is non zero only for adjacent vertices of m setOfMST[vertex] is false for vertices not yet included in Minimum Spanning Tree
                // when the value of the graphArray[edge][vertex] is smaller than key[vertex], we update the key
                if (graphArray[edge][vertex] != 0 && setOfMST[vertex] == false && graphArray[edge][vertex] < keys[vertex]) {
                    mstArray[vertex] = edge;
                    keys[vertex] = graphArray[edge][vertex];
                }
        }

        // print the constructed Minimum Spanning Tree
        showMinimumSpanningTree(mstArray, graphArray);
    }

    public int[][] convertGraphToAdjMatrix(Graph graph) {
        setCountOfVertices(graph.getGraph().size());
        int graphArray[][] = new int[getCountOfVertices()][getCountOfVertices()];
        mapVertexNameAndId = new HashMap<String, Integer>();

        //her bir vertex in name ine karsilik bir indis atandi
        int i = 0;
        for (Vertex vertex : graph.getGraph().keySet()) {
            mapVertexNameAndId.put(vertex.getName(), i);
            ++i;
        }
        /*System.out.println("Vertex Name: Edge id");
        mapVertexNameAndId.forEach((s, integer) -> System.out.println(s +": "+integer));*/

        //bag olanlar 1 digerleri 0 atandi
        graph.getGraph().forEach((vertex, edges) -> {
            edges.forEach(edge -> {
                int id1 = mapVertexNameAndId.get(vertex.getName());
                int id2 = mapVertexNameAndId.get(edge.getVertex().getName());
                graphArray[id1][id2] = 1;
                graphArray[id2][id1] = 1;
            });
        });

        return graphArray;
    }

    public void designMST(Graph graph) {
        designMST(convertGraphToAdjMatrix(graph));
    }

    //main() method start
    /*public static void main(String[] args) {

        PrimAlgorithm mst = new PrimAlgorithm();
        int graphArray[][] = new int[][]{
                {0, 4,  0, 0,  0,  0,  0, 8,  0},
                {4, 0,  8, 0,  0,  0,  0, 11, 0},
                {0, 8,  0, 7,  0,  4,  0, 0,  2},
                {0, 0,  7, 0,  9,  14, 0, 0,  0},
                {0, 0,  0, 9,  0,  10, 0, 0,  0},
                {0, 0,  4, 14, 10, 0,  2, 0,  0},
                {0, 0,  0, 0,  0,  2,  0, 1,  6},
                {8, 11, 0, 0,  0,  0,  1, 0,  7},
                {0, 0,  2, 0,  0,  0,  6, 7,  0}};

        // Print the Minimum Spanning Tree solution
        mst.designMST(graphArray);
    }*/


}
