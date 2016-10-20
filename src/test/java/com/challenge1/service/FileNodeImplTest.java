package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by mlgy on 19/10/2016.
 */
public class FileNodeImplTest {

    @Test
    public void shouldReturnSth() { //integration test
        //given

        FileNodeImpl fileRoot = new FileNodeImpl(Paths.get("c:/root"));
        Iterable<Node> nodeIterator = NodeLogic.getNodeIterator(fileRoot);
        //when
        Iterator<Node> iterator = nodeIterator.iterator();
        //then
        List<String> pathList = new ArrayList<>();
        while (iterator.hasNext()) {
            Node next = iterator.next();


            System.out.println("path : [" + next.getData() + "]");
            Assert.assertThat(pathList.contains(next.getData().toString()), is(false));
            pathList.add(next.getData().toString());
        }
    }

}