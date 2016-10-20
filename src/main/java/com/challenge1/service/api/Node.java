package com.challenge1.service.api;


/**
 *
 */
public interface  Node<E> extends Iterable<Node> {
    Iterable<Node> getChildren();
    E getData();
}
