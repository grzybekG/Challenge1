package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;

/**
 * Created by mlgy on 2016-09-28.
 */
public class NodeLogicTest {

    @Test
    public void shouldHaveNoChilds() {
        Node root = new NodeImpl();
        Iterator<Node> leafIterator = NodeLogic.getChildrenIterator(root);

        assertFalse(leafIterator.hasNext());

    }

    @Test
    public void shouldRetNestedLeafs() {

        Node leaf1 = new NodeImpl();
        Node leaf2 = new NodeImpl(leaf1);
        Node root = new NodeImpl(leaf2);

        Iterator<Node> branchIterator = NodeLogic.getChildrenIterator(root);

        Assert.assertThat(branchIterator.hasNext(), is(equalTo(true)));
        List<Node> resultList = new ArrayList<>();
        while (branchIterator.hasNext()){
            resultList.add(branchIterator.next());

        }


        Assert.assertThat(resultList, containsInAnyOrder(leaf1, leaf2));
    }


}