package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.challenge1.service.api.Leaf;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class BranchImpl implements Branch{
    List<Branch> branches;

    @Override
    public Iterator<Branch> iterator() {
        return branches.iterator();
    }

    public BranchImpl(Branch... branches) {
        this.branches = Arrays.asList(branches);
    }

}