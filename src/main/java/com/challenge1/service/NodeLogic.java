package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class NodeLogic {
    public static <E extends Node> Iterator<Node> getNodeIterator(E root) {
        return root.getChildren();
    }



}
