package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.Iterator;

/**
 * Created by mlgy on 12/10/2016.
 */
public class NodeImpl implements Node {

    NodeIterator nodeIterator;
    private String path;

    public NodeImpl(String path, Node... root) {
        this.path = path;
        nodeIterator = new NodeIterator(root);
    }

    @Override
    public Iterator<Node> getChildren() {
        return nodeIterator;
    }

    @Override
    public String getData() {
        return path;
    }
}
