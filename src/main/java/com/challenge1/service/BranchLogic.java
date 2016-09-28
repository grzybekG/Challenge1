package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.challenge1.service.api.Leaf;

import java.util.Iterator;

/**
 * Created by mlgy on 2016-09-28.
 */
public class BranchLogic {
    public static Iterator<Leaf> gatherBranchIterator(Branch root){

        return root.iterator();
    }
}
