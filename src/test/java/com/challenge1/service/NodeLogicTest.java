package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.challenge1.service.NodeLogic.getNodeIterator;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;

public class NodeLogicTest {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Test
    public void shouldHaveNoChild() {
        Node<String> root = new NodeImpl("root");
        Iterator<Node<String>> nodeIterator = getNodeIterator(root).iterator();

        assertFalse(nodeIterator.hasNext());

    }

    @Test
    public void shouldReturnAllLeafs() {
        Node<String> one = new NodeImpl("one");
        Node<String> two = new NodeImpl("two");
        Node<String> three = new NodeImpl("three");
        Node<String> root = new NodeImpl("path",Arrays.asList(one, two, three));

        Iterator<Node<String>> nodeIterator = getNodeIterator(root).iterator();
        ImmutableList<Node<?>> nodes = ImmutableList.copyOf(nodeIterator);
        Assert.assertThat(nodes, containsInAnyOrder(one, two, three));
    }

    @Test

    public void shouldRetNestedLeafs() {
        Node<String> leaf1 = new NodeImpl("leaf1");
        Node<String> leaf2 = new NodeImpl("leaf2", leaf1);
        Node<String> root = new NodeImpl("root", leaf2);

        Iterator<Node<String>> nodeIterator = getNodeIterator(root).iterator();
        ImmutableList<Node<?>> nodes = ImmutableList.copyOf(nodeIterator);

        Assert.assertThat(nodes, containsInAnyOrder(leaf1, leaf2));
        Assert.assertThat(nodes.size(), is(2));

    }

    @Test
    public void shouldRetNestedLeafsFromAllBranches() {
        //given
        Node<String> leaf1 = new NodeImpl("leaf1");
        Node<String> leaf2 = new NodeImpl("leaf2", leaf1);
        Node<String> leaf3 = new NodeImpl("leaf3", leaf2);
        Node<String> leaf4 = new NodeImpl("leaf4", leaf3);
        Node<String> leaf5 = new NodeImpl("leaf5");
        Node<String> leaf6 = new NodeImpl("leaf6", leaf5);
        Node<String> root = new NodeImpl("root", Arrays.asList(leaf4, leaf6));
        //when
        Iterator<Node<String>> nodeIterable = getNodeIterator(root).iterator();

        //then
        List<Node<?>> resultList = new ArrayList<>();

        while (nodeIterable.hasNext()) {
            Node next = nodeIterable.next();
            LOG.info("Element retrieved is {}", next.getData());
            resultList.add(next);
        }

        Assert.assertThat(resultList, containsInAnyOrder(leaf1, leaf2, leaf3, leaf4, leaf5, leaf6));
        Assert.assertThat(resultList.size(), is(6));
    }

    @Test
    public void shouldRetNestedLeafsFromAllBranchesWithMultipleLeafsOnNested() {
        //given
        Node<String> leaf1 = new NodeImpl("leaf1");
        Node<String> leaf1a = new NodeImpl("leaf1a");
        Node<String> leaf2 = new NodeImpl("leaf2", Arrays.asList(leaf1, leaf1a));
        Node<String> leaf3 = new NodeImpl("leaf3", leaf2);
        Node<String> leaf4 = new NodeImpl("leaf4", leaf3);
        Node<String> leaf5 = new NodeImpl("leaf5");
        Node<String> leaf6 = new NodeImpl("leaf6", leaf5);
        Node<String> root = new NodeImpl("root", Arrays.asList(leaf4, leaf6));

        //when
        Iterator<Node<String>> iterator = getNodeIterator(root).iterator();

        //then
        List<Node<?>> resultList = new ArrayList<>();

        while (iterator.hasNext()) {
            Node next = iterator.next();
            LOG.info("Element retrieved is {}", next.getData());
            resultList.add(next);
        }

        Assert.assertThat(resultList,containsInAnyOrder(leaf1, leaf1a, leaf2, leaf3, leaf4, leaf5, leaf6));
        Assert.assertThat(resultList.size(), is(7));
    }

    @Test(expected = NullPointerException.class)
    public void notValidNullLeaf() {
        Node<String> leaf1a = new NodeImpl("leaf1a");
        Node<String> root = new NodeImpl("leaf2",Arrays.asList(null, leaf1a));

        Iterator<Node<String>> iterator = getNodeIterator(root).iterator();
        ImmutableList.copyOf(iterator);

    }
    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionWhenNoNextElement() {

        Node<String> root = new NodeImpl("leaf2");

        Iterator<Node<String>> iterator = getNodeIterator(root).iterator();
        Assert.assertThat(iterator.hasNext(), is(false));

        iterator.next(); //expecting npe from here
    }
}