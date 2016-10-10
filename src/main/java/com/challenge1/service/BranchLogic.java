package com.challenge1.service;

import com.challenge1.service.api.Branch;
import com.challenge1.service.api.Leaf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by mlgy on 2016-09-28.
 */
public class BranchLogic {
    static Stack<Branch> stack = new Stack<>();


    public static Iterator<Branch> gatherBranchIterator(Branch root) {
        goForNestedNodes(root);

        return stack.iterator();
    }

    private static void goForNestedNodes(Branch branch) {
        Iterator<Branch> iterator = branch.iterator();
        while (iterator.hasNext()) {
            iterator.forEachRemaining(anotherBranch -> addToResult(anotherBranch));
        }


    }

    private static void addToResult(Branch branch) {
        Iterator<Branch> iterator = branch.iterator();
        stack.push(branch);
        while (iterator.hasNext()) {
            Branch nestedBranch = iterator.next();
            stack.push(nestedBranch);
            goForNestedNodes(nestedBranch);

        }


    }

    private static List<Leaf> gatherLeafs(List<Iterator<Leaf>> tmpIteratorStack) {
        List<Leaf> leafList = new ArrayList<>();
        for (Iterator<Leaf> leafIterator : tmpIteratorStack) {
            while (leafIterator.hasNext()) {
                leafList.add(leafIterator.next());
            }
        }
        return leafList;
    }

    /*
        Iterator<Leaf> iterator = root.iterator();
        while (iterator.hasNext()){
            Leaf potentialBrach= iterator.next();
            Iterator<Leaf> iterator1 = potentialBrach.iterator();
            if(iterator1.hasNext()){
                Leaf next = iterator1.next();

                //todo
            }else
                tmpStack.add(potentialBrach);



            tmpStack.add(potentialBrach);
        }



        return tmpStack.iterator();*/
}
