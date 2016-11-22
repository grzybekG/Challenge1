package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.springframework.stereotype.Service;

@Service
public class NodeLogic {

  public <E> Iterable<Node<E>> getNodeIterator(Node<E> root) {
        return new NodeIterator<>(root);
    }

}
