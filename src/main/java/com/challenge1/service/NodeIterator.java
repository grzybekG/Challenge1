package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.Iterator;
import java.util.Stack;

 class NodeIterator<E> implements Iterable<Node<E>> {
    private Stack<Iterator<Node<E>>> nodeStack = new Stack<>();

    /**
     *
     * @param root not null instance of Node implementation
     *
     */
    NodeIterator(Node<E> root) throws IllegalArgumentException{
        nodeStack.push(root.iterator());

    }

    @Override
    public Iterator<Node<E>> iterator() {
        return new Iterator<Node<E>>() {
            @Override
            public boolean hasNext() {
                for (Iterator<?> single : nodeStack) {
                    if (single.hasNext()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Node<E> next() {
//              FIXME  if (!hasNext()) {
//                    throw new NoSuchElementException("no next element.");
//                }

                Iterator<Node<E>> peek = nodeStack.peek();
                if (peek.hasNext()) {
                    Node<E> next = peek.next();
                    if (next != null && next.getChildren() != null) {
                        Iterator<Node<E>> childrenIterator = next.iterator();
                        if (childrenIterator.hasNext()) {
                            nodeStack.push(childrenIterator);
                        }
                    }if(!peek.hasNext()){
                        nodeStack.remove(peek);
                    }
                    return next;
                }

                nodeStack.remove(peek);
                return next();
            }
        };
    }
}
