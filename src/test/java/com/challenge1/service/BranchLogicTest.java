package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.challenge1.service.api.Leaf;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by mlgy on 2016-09-28.
 */
public class BranchLogicTest {

    @Test
    public void test(){
        Branch root = new BranchImpl();
        Iterator<Leaf> leafIterator = BranchLogic.gatherBranchIterator(root);

        assertFalse(leafIterator.hasNext());

    }



}