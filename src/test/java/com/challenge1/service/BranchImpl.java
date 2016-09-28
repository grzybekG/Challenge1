package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.challenge1.service.api.Leaf;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class BranchImpl implements Branch {
    List<Leaf> leafs;

    @Override
    public Iterator<Leaf> iterator() {
        return leafs.iterator();
    }

    public BranchImpl(Leaf... leafs) {
        this.leafs = Arrays.asList(leafs);
    }
}