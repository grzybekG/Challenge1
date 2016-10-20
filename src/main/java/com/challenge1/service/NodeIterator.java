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
    private Stack<Node> nodeStack = new Stack<>();
    private Iterator<Node> tmpIterator;

    public NodeIterator(Node[] nodes) {
        for (Node node : nodes) {
            nodeStack.push(node);
        }
    }

    @Override
    public boolean hasNext() {
        return !nodeStack.empty();
    }

    /**
     * return next node when there is no node
     * throw IndexOutOfBoundsException when no next element
     */
    @Override
    public Node next() {
        if (!hasNext()) {
            throw new IndexOutOfBoundsException("no next element.");
        }
        if ((tmpIterator == null || !tmpIterator.hasNext()) && !nodeStack.empty()) {
            tmpIterator = nodeStack.peek().iterator();
        }
        if (tmpIterator.hasNext()) {
            while (tmpIterator.hasNext()) {
               nodeStack.push(tmpIterator.next());
            }
        } else {
            Node pop = nodeStack.pop();
            LOG.info("Stack size is {}, obj returned {}", nodeStack.size(), pop);

            return pop;
        }
        return next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot perform this operation");
    }

}
