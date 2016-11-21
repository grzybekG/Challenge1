package com.challenge1.service;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.api.Type;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ObserverServiceImplTest {

    @Before
    public void setup() {
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        String path = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");
        try {
            FileUtils.forceDelete(new File(path + "/test1"));
        } catch (IOException e) {
            //cleaning for integration tests
        }
        try {
            FileUtils.forceDelete(new File(path + "/test2"));
        } catch (IOException e) {
            //cleaning for integration tests
        }
    }

    @Test
    public void shouldCreateNotNullObservableFoeExistingPath() throws Exception {
        ObservableService observerService = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateNotNullObservableFoeNonExistingPath() throws Exception {
        ObservableService observerService = new ObserverServiceImpl();
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(null);
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateObservableFormPath() throws Exception {
        ObservableService observerService = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        List<Node<?>> result = new ArrayList<>();
        observableForPath.subscribe(node -> result.add(node));

        Assert.assertThat(result.size(), is(7));
    }

    @Test
    public void integrationTestSimpleFolderChange() throws Exception {
        //given
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        String path = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");

        ObserverServiceImpl service = new ObserverServiceImpl();
        Observable<Node<?>> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));

        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber();
        testSubscriber.getOnNextEvents();
        directoryWatcherObservable.subscribe(testSubscriber);

        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));

        delay(1000l);
        //then
        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();


        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500l, TimeUnit.MICROSECONDS);
        Assert.assertThat(emittedEvents.size(), is(2));
        for (Node<?> node : emittedEvents) {
            Assert.assertThat(node.getType(), is(Type.ENTRY_CREATE));
        }
    }

    private synchronized void delay(long l) throws InterruptedException {
        wait(l);
    }

    @Test
    public void integrationTestNestedFolders() throws Exception {
        //given
        ObserverServiceImpl service = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        String path = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");
        Observable<Node<?>> directoryWatcherObservable = service.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribe(testSubscriber);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir"));

        ///then


        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(1500l, TimeUnit.MICROSECONDS);
        //TODO check emmitted events are one you expected

    }
}
