package org.elasticgremlin.queryhandler.virtualvertex;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.apache.tinkerpop.gremlin.structure.*;
import org.elasticgremlin.queryhandler.*;
import org.elasticgremlin.structure.*;

import java.util.*;

public class VirtualVertexHandler implements VertexHandler {

    private static final long VERTEX_BULK = 1000;

    private ElasticGraph graph;
    private String label;
    private List<BaseVertex> vertices;

    public VirtualVertexHandler(ElasticGraph graph, String label) {
        this.graph = graph;
        this.label = label;
        this.vertices = new ArrayList<>();
    }

    @Override
    public Iterator<Vertex> vertices() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Vertex> vertices(Object[] vertexIds) {
        ArrayList<BaseVertex> vertices = new ArrayList<>();
        for(Object id : vertexIds) {
            BaseVertex vertex = new VirtualVertex(id, label, graph, null);
            vertices.add(vertex);
            vertex.setSiblings(vertices);
        }
        return new ArrayIterator<>((Vertex[]) vertices.toArray()).iterator();
    }

    @Override
    public Iterator<Vertex> vertices(Predicates predicates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BaseVertex vertex(Object vertexId, String vertexLabel, Edge edge, Direction direction) {
        checkBulk();
        BaseVertex vertex = new VirtualVertex(vertexId, vertexLabel, graph, null);
        vertex.setSiblings(vertices);
        vertices.add(vertex);
        return vertex;
    }

    private void checkBulk() {
        if (vertices.size() >= VERTEX_BULK) {
            vertices = new ArrayList<>();
        }
    }

    @Override
    public BaseVertex addVertex(Object id, String label, Object[] properties) {
        throw new UnsupportedOperationException();
    }
}
