package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

class NodeIterator<E> implements Iterable<Node<E>> {
    private Stack<Iterator<Node<E>>> nodeStack = new Stack<>();

    /**
     * @param root not null instance of Node implementation
     */
    NodeIterator(Node<E> root) throws IllegalArgumentException {
        Iterator<Node<E>> tmpIterator = root.iterator();
        if (tmpIterator.hasNext()) {
            nodeStack.push(tmpIterator);
        }

    }

    @Override
    public Iterator<Node<E>> iterator() {
        return new Iterator<Node<E>>() {
            @Override
            public boolean hasNext() {
                return !nodeStack.empty();
            }

            @Override
            public Node<E> next() {
                if (nodeStack.empty()) {
                    throw new NoSuchElementException("no next element.");
                }

                Iterator<Node<E>> currentIterator = nodeStack.pop();

                Node<E> next = currentIterator.next();
                if (currentIterator.hasNext()) {
                    nodeStack.add(currentIterator);
                }

                if (next != null && next.getChildren() != null) {
                    Iterator<Node<E>> childrenIterator = next.iterator();
                    if (childrenIterator.hasNext()) {
                        nodeStack.push(childrenIterator);
                    }
                }else{
                    System.out.println("DATA: " +next.getData());
                }
                return next;
            }
        };
    }
}
