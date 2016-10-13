package com.challenge1.service.api;


import java.util.Iterator;

/**
 *
 */
public interface Node extends Iterable<Node> {
    Iterator<Node> getChildren();
    String getData();
}
