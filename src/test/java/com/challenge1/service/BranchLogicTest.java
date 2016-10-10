package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
public class BranchLogicTest {

    @Test
    public void shouldHaveNoChilds() {
        Branch root = new BranchImpl();
        Iterator<Branch> leafIterator = BranchLogic.gatherBranchIterator(root);

        assertFalse(leafIterator.hasNext());

    }

    @Test
    public void shouldRetNestedLeafs() {

        Branch leaf1 = new BranchImpl();
        Branch leaf2 = new BranchImpl(leaf1);
        Branch root = new BranchImpl(leaf2);

        Iterator<Branch> branchIterator = BranchLogic.gatherBranchIterator(root);

        Assert.assertThat(branchIterator.hasNext(), is(equalTo(true)));
        List<Branch> resultList = new ArrayList<>();
        while (branchIterator.hasNext()){
            resultList.add(branchIterator.next());

        }


        Assert.assertThat(resultList, containsInAnyOrder(leaf1, leaf2));
    }


}