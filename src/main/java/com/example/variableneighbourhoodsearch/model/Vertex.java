package com.example.variableneighbourhoodsearch.model;

import lombok.*;
import org.springframework.util.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vertex {
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)){
            return true;
        }
        if(!(obj instanceof Vertex)){
            return false;
        }
        Vertex v = (Vertex) obj;
        return this.name != null && this.name.equals(v.getName());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        return prime + (name == null ? 0 : name.hashCode());
    }
}
