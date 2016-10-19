package com.challenge1.service.api;


import java.util.Iterator;

/**
 *
 */
public interface  Node<E> {
    Iterator<Node<E>> getChildren();
    E getData();
}
