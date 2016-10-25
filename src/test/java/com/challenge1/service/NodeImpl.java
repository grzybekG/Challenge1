package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.google.common.base.Preconditions;

import java.util.*;

class NodeImpl implements Node<String> {

    private List<Node<String>> nodes = new ArrayList<>();
    private String path;

    /**
     * E
     *
     * @param path  Meaningful path/name
     * @param nodes not null nodes vararg list
     */

    NodeImpl(String path, List<Node<String>> nodes) {
        for (Node<String> node : nodes) {
            Preconditions.checkNotNull(node);
            this.nodes.add(node);
        }
        this.path = path;
    }

    NodeImpl(String path) {
        this.path = path;
    }

    NodeImpl(String path, Node<String> leaf) {
        this.path=path;
        this.nodes = Arrays.asList(leaf);
    }




    @Override
    public Iterable<Node<String>> getChildren() {
        return nodes;
    }

    @Override
    public String getData() {
        return path;
    }

    @Override
    public Iterator<Node<String>> iterator() {
        return nodes.iterator();
    }
}
