package com.challenge1.service.api;


public interface Node<E> extends Iterable<Node<E>> {

    Iterable<Node<E>> getChildren();
    E getData();
}
