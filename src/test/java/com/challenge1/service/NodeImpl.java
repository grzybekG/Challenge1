package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class NodeImpl implements Node {
    List<Node> nodes;

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public NodeImpl(Node... nodes) {
        this.nodes = Arrays.asList(nodes);
    }

}