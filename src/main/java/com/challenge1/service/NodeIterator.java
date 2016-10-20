package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by mlgy on 12/10/2016.
 */
public class NodeIterator implements Iterator<Node> {
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Stack<Iterator<Node>> nodeStack = new Stack<>();
    //private Iterator<Node> tmpIterator;

    public NodeIterator(List<Node> nodes) {
        nodeStack.push(nodes.iterator());
    }

    @Override
    public boolean hasNext() {

        for (Iterator<Node> single : nodeStack) {
            if (single.hasNext()) {
                return true;
            }

        }
        return false;
    }

        /**
         * return next node when there is no node
         * throw IndexOutOfBoundsException when no next element
         */
        @Override
        public Node next () {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("no next element.");
            }

            Iterator<Node> peek = nodeStack.peek();
            if (peek.hasNext()) {
                Node next = peek.next();
                if (next != null && next.getChildren() != null) {
                    Iterator<Node> childrenIterator = next.iterator();
                    if (childrenIterator.hasNext()) {
                        nodeStack.push(childrenIterator);
                    }
                }
                return next;

            } else {
                nodeStack.remove(peek);

                return next();
            }
        }

        @Override
        public void remove () {
            throw new UnsupportedOperationException("Cannot perform this operation");
        }

    }
