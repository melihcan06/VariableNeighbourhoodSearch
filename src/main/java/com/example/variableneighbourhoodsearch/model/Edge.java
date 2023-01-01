package com.example.variableneighbourhoodsearch.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Edge {
    private Vertex vertex;//second vertex of edge
    private String label;

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)){
            return true;
        }
        if(!(obj instanceof Edge)){
            return false;
        }
        Edge v = (Edge) obj;
        return this.vertex != null && this.vertex.equals(v.getVertex()) && this.label != null && this.label.equals(v.getLabel());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return prime * (vertex == null ? 0 : vertex.hashCode()) + (label == null ? 0 : label.hashCode());
    }

    @Override
    public String toString() {
        return (vertex == null?"":(vertex.getName() == null?"":vertex.getName()))+Constants.edgeSeperator +(label==null?"":label);
    }
}
