package com.challenge1.service;

import com.challenge1.service.api.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by mlgy on 2016-09-28.
 */
public class NodeLogic {
    static Stack<Iterator<Node>> nodeToProcess = new Stack<>();

    private static Iterator<Node> getChildrenIterator(Node root) {
        Iterator<Node> iterator = root.iterator();
        while (iterator.hasNext()) {
            nodeToProcess.push(iterator.next().iterator());
        }

        while (!nodeToProcess.isEmpty()){
            Iterator<Node> peek = nodeToProcess.peek();

        }


        return null;
    }

    private static void goForNestedNodes(Node node) {
        Iterator<Node> iterator = node.iterator();
        while (iterator.hasNext()) {
            iterator.forEachRemaining(anotherBranch -> addToResult(anotherBranch));
        }


    }

    private static void addToResult(Node node) {
        Iterator<Node> iterator = node.iterator();
        stack.push(node);
        while (iterator.hasNext()) {
            Node nestedNode = iterator.next();
            stack.push(nestedNode);
            goForNestedNodes(nestedNode);

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
