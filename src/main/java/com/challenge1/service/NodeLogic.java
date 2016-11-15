package com.challenge1.service;

import com.challenge1.service.api.Node;

public class NodeLogic {
  public static <E> Iterable<Node<E>> getNodeIterator(Node<E> root) {

        return new NodeIterator<>(root);
    }

}
