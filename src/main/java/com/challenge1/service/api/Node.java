package com.challenge1.service.api;

/**
 *
 * @param <E>
 */
public interface Node<E> extends Iterable<Node<E>> {

    Iterable<Node<E>> getChildren();
    E getData();

}
