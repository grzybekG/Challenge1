package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mlgy on 12/10/2016.
 */
public class NodeImpl implements Node {

    private NodeIterator nodeIterator;
    private List<Node> nodes;
    private String path;


    public NodeImpl(String path, Node... root) {
        this.path = path;
        this.nodes = Arrays.asList(root);
    }
    public NodeImpl(String path, List<Node> nodes) {
        this.path = path;
        this.nodes = nodes;
    }

    @Override
    public Iterable<Node> getChildren() {
        return new NodeImpl(path, nodes);
    }

    @Override
    public String getData() {
        return path;
    }

    @Override
    public Iterator iterator() {
        if (nodeIterator == null) {
            nodeIterator = new NodeIterator(nodes);
        }
        return nodeIterator;
    }
}
