package com.challenge1.service.api;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.ObserverServiceImpl;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;


public class StructureWatcherTest {
    @Test
    public void integrationTestSimpleFolderChange() throws Exception {
        //given
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        String path  = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");
        List<String> strings = Arrays.asList("test1", "test2");

        ObserverServiceImpl service = new ObserverServiceImpl();
        Observable<Node<?>> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));

        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();

        directoryWatcherObservable.subscribe(testSubscriber);
        //when
        Files.createDirectory(Paths.get(path+"/test1"));
        Files.createDirectory(Paths.get(path+"/test2"));


        //then
        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500l, TimeUnit.MICROSECONDS);

        for (Node<?> node : emittedEvents) {
            Assert.assertTrue(strings.contains(node.getData().toString()));
            Assert.assertThat(node.getType(), is(Type.ENTRY_CREATE));
        }
        //cleanUp
        FileUtils.forceDelete(new File(path+"/test1"));
        FileUtils.forceDelete(new File(path+"/test2"));

    }

    @Test
    public void integrationTestNestedFolders() throws Exception {
        //given
        ObserverServiceImpl service = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        String path = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");
        Observable<Node<?>> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        List<String> expectedResult =Arrays.asList("test1","nestedDir");
        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribe(testSubscriber);
        Files.createDirectory(Paths.get(path+ "/test1"));
        Files.createDirectory(Paths.get(path+ "/test1/nestedDir"));

        ///then


        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(1500l, TimeUnit.MICROSECONDS);

        for (Node<?> node : emittedEvents) {
            Assert.assertTrue(expectedResult.contains(node.getData().toString()));
            Assert.assertThat(node.getType(), is(Type.ENTRY_CREATE));
        }
        FileUtils.forceDelete(new File(path+"/test1"));
    }

    @Test
    public void integrationTest2NestedFolders() throws Exception {

        ObserverServiceImpl service = new ObserverServiceImpl();
        Observable<Node<?>> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get("c:\\dev"));
        List<Node<?>> resultList = new ArrayList<>();
        directoryWatcherObservable.subscribe(node -> {
            System.out.println("There was an action [" + node.getType() + "] for path [" + node.getData().toString() + "]");
            resultList.add(node);

        });


        Files.createDirectory(Paths.get("c:\\test\\test1"));

        Files.createDirectory(Paths.get("c:\\test\\test1\\nestedDir"));

        Files.createDirectory(Paths.get("c:\\test\\test1\\nestedDir\\nestedMore"));

        FileUtils.deleteDirectory(new File("c:\\test\\test1"));

        Assert.assertThat(resultList.size(), is(4));
    }


}