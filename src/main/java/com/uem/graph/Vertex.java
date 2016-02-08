package com.uem.graph;

import com.uem.dbstructure.Table;

/**
 * Represents a Vertex in the Graph
 *
 * @author zessin
 */
public class Vertex {
    private String name;
    private Long degree;

    /**
     * Initializes the Vertex with a name
     * @param name The name of the Vertex
     */
    public Vertex(String name) {
        super();
        this.name = name;
        degree = 0l;
    }

    /**
     * Initializes the Vertex using the Table's name
     * @param table The Table which the Vertex will represent in the Graph
     */
    public Vertex(Table table) {
        super();
        name = table.getName();
        degree = 0l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public void increaseDegree() {
        degree++;
    }

    public boolean isDegreePositive() {
        return degree > 0;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Vertex)) {
            return false;
        }

        final Vertex otherVertex = (Vertex) other;
        return this.getName().equals(otherVertex.getName());
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
