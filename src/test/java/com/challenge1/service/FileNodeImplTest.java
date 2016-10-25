package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

 public class FileNodeImplTest {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private static String TEST_RESOURCE_PATH = "\\src\\test\\resources\\ParentFolder";

    @BeforeClass
    public static void setUp() {
        TEST_RESOURCE_PATH = new File(".").getAbsolutePath() + TEST_RESOURCE_PATH;
        System.out.print("TEST RESOURCE PATH: [" + TEST_RESOURCE_PATH + "].");
    }

    @Test

    public void shouldReturnStructureWithoutRepetitions() { //integration test
        //given

        FileNodeImpl fileRoot = new FileNodeImpl(Paths.get("c:/dev"));
        Iterable<Node<Path>> nodeIterator = NodeLogic.getNodeIterator(fileRoot);

        //////////////////////

        ObservableServiceImpl.getObservable(nodeIterator);

        ////////////////////


        //when
        Iterator<Node<Path>> iterator = nodeIterator.iterator();
        //then
        List<String> pathList = new ArrayList<>();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            System.out.println("path : [" + next.getData() + "]");
            Assert.assertThat(pathList.contains(next.getData().toString()), is(false));
            pathList.add(next.getData().toString());
        }
    }

    @Test
    public void shouldReturnEmptyFolderIterator() throws Exception {

        FileNodeImpl fileIterable = new FileNodeImpl(Paths.get(TEST_RESOURCE_PATH + "/SubFolder1/EmptyFolder"));
        Iterator<Node<Path>> iterator = fileIterable.iterator();
        ImmutableList<Node> nodes = ImmutableList.copyOf(iterator);
        for (Node node : nodes) {
            LOG.info("Path  is [" + node.getData() + "]");
        }
        Assert.assertThat(nodes, is(empty()));
        Assert.assertThat(nodes.size(), Matchers.is(0));
    }

    @Test
    public void shouldReturnWholeStructure() throws Exception {
        Iterable<Node<Path>> nodes = NodeLogic.getNodeIterator(new FileNodeImpl(Paths.get(TEST_RESOURCE_PATH)));

        Iterator<Node<Path>> iterator = nodes.iterator();
        ImmutableList<Node> result = ImmutableList.copyOf(iterator);
        for (Node node : result) {
            LOG.info("Path  is" + node.getData());
        }
        Assert.assertThat(result.size(), Matchers.is(7));
    }


}