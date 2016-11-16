package com.challenge1.service;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.api.Node;
import com.google.common.collect.ImmutableList;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class FileHandlerImplTest {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Test
    public void shouldReturnStructureWithoutRepetitions() throws Exception { //integration test
        //given
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        FileHandlerImpl fileRoot = new FileHandlerImpl(Paths.get(parentFolder.toURI()));
        Iterable<Node<Path>> nodeIterator = NodeLogic.getNodeIterator(fileRoot);

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
        URL pathFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder/SubFolder1/subFile1");
        FileHandlerImpl fileIterable = new FileHandlerImpl(Paths.get(pathFolder.toURI()));
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
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        FileHandlerImpl fileRoot = new FileHandlerImpl(Paths.get(parentFolder.toURI()));
        Iterable<Node<Path>> nodeIterator = NodeLogic.getNodeIterator(fileRoot);

        Iterator<Node<Path>> nodes = nodeIterator.iterator();
        ImmutableList<Node> result = ImmutableList.copyOf(nodes);


        Assert.assertThat(result, Matchers.is(notNullValue()));
        Assert.assertThat(result.size(), Matchers.is(7));
    }


}