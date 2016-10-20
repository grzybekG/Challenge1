package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;

/**
 * Created by mlgy on 2016-09-28.
 */
public class NodeLogicTest {
    Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Test
    public void shouldHaveNoChild() {
        Node root = new NodeImpl("root");
        Iterator<Node> nodeIterator = root.getChildren().iterator();

        assertFalse(nodeIterator.hasNext());

    }

    @Test
    public void shouldReturnAllLeafs() {
        Node one = new NodeImpl("one");
        Node two = new NodeImpl("two");
        Node three = new NodeImpl("three");
        Node root = new NodeImpl("path", one, two, three);

        Iterator<Node> nodeIterator = NodeLogic.getNodeIterator(root).iterator();

        Assert.assertThat(ImmutableList.copyOf(nodeIterator), containsInAnyOrder(one, two, three));
    }

    @Test
    public void shouldRetNestedLeafs() {

        Node leaf1 = new NodeImpl("leaf1");
        Node leaf2 = new NodeImpl("leaf2", leaf1);
        Node root = new NodeImpl("root", leaf2);
        Iterator<Node> iterator = NodeLogic.getNodeIterator(root).iterator();

        ImmutableList<Node> nodes = ImmutableList.copyOf(iterator);
        Assert.assertThat(nodes, containsInAnyOrder(leaf1, leaf2));
        Assert.assertThat(nodes.size(), is(2));

    }

    @Test
    public void shouldRetNestedLeafsFromAllBranches() {
        //given
        Node leaf1 = new NodeImpl("leaf1");
        Node leaf2 = new NodeImpl("leaf2", leaf1);
        Node leaf3 = new NodeImpl("leaf3", leaf2);
        Node leaf4 = new NodeImpl("leaf4", leaf3);
        Node leaf5 = new NodeImpl("leaf5");
        Node leaf6 = new NodeImpl("leaf6", leaf5);
        Node root = new NodeImpl("root", leaf4, leaf6);
        //when
        Iterable<Node> nodeIterator = NodeLogic.getNodeIterator(root);
        Iterator<Node> iterator = nodeIterator.iterator();
        //then
        ImmutableList<Node> nodes = ImmutableList.copyOf(iterator);
        Assert.assertThat(nodes, containsInAnyOrder(leaf1, leaf2, leaf3, leaf4, leaf5, leaf6));
        Assert.assertThat(nodes.size(), is(6));
    }

    @Test
    public void shouldRetNestedLeafsFromAllBranchesWithMultipleLeafsOnNested() {
        //given
        Node leaf1 = new NodeImpl("leaf1");
        Node leaf1a = new NodeImpl("leaf1a");
        Node leaf2 = new NodeImpl("leaf2", leaf1, leaf1a);
        Node leaf3 = new NodeImpl("leaf3", leaf2);
        Node leaf4 = new NodeImpl("leaf4", leaf3);
        Node leaf5 = new NodeImpl("leaf5");
        Node leaf6 = new NodeImpl("leaf6", leaf5);
        Node root = new NodeImpl("root", leaf4, leaf6);

        //when
        Iterator<Node> iterator = NodeLogic.getNodeIterator(root).iterator();

        //then
        List<Node> resultList = new ArrayList<>();

        while (iterator.hasNext()) {
            Node next = iterator.next();
            LOG.info("Element retrieved is {}", next.getData());
            resultList.add(next);
        }

        Assert.assertThat(resultList, containsInAnyOrder(leaf1, leaf1a, leaf2, leaf3, leaf4, leaf5, leaf6));
        Assert.assertThat(resultList.size(), is(7));
    }
    @Test
    public void notValidNullLeaf(){
        Node leaf1 = null;
        Node leaf1a = new NodeImpl("leaf1a");
        Node root = new NodeImpl("leaf2", leaf1, leaf1a);

        Iterator<Node> iterator = NodeLogic.getNodeIterator(root).iterator();
    }
    @Test
    public void notValidLeafWithNull(){
        Node leaf1 = new NodeImpl(null);
        Node leaf1a = new NodeImpl("leaf1a");
        Node root = new NodeImpl("leaf2", leaf1, leaf1a);

        Iterator<Node> iterator = NodeLogic.getNodeIterator(root).iterator();
    }



}