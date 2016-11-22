package com.challenge1.service;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.api.Type;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.spy;

public class ObserverServiceImplTest {
    private URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
    private String path = parentFolder.getPath().replaceFirst("^/(.:/)", "$1");
    private NodeLogic nodeLogic;
    private ObserverServiceImpl observerService;

    @Before
    public void setUp() {
        nodeLogic = spy(NodeLogic.class);
        observerService = new ObserverServiceImpl(nodeLogic);
    }

    @After
    public void tearDown() {
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
        ReflectionTestUtils.setField(observerService, "nodeLogic", nodeLogic);
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateNotNullObservableFoeNonExistingPath() throws Exception {
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(null);
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateObservableFormPath() throws Exception {
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        List<Node<?>> result = new ArrayList<>();
        observableForPath.subscribe(node -> result.add(node));

        Assert.assertThat(result.size(), is(7));
    }

    @Test //
    public void integrationTestSimpleFolderChange() throws Exception {
        //given
        Observable<Node<?>> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));

        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber();
        testSubscriber.getOnNextEvents();
        directoryWatcherObservable.subscribe(testSubscriber);

        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));

        delay(500l);
        //then
        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();


        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500l, TimeUnit.MICROSECONDS);
        Assert.assertThat(emittedEvents.size(), is(2));
        for (Node<?> node : emittedEvents) {
            Assert.assertThat(node.getType(), is(Type.ENTRY_CREATE));
        }
    }

    @Test
    public void integrationTestNestedFolders() throws Exception {
        //given

        Observable<Node<?>> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribe(testSubscriber);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir"));

        ///then
        delay(500l);

        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();


        //TODO check emitted events are one you expected
        Assert.assertThat(emittedEvents.size(), is(2));

    }

    @Test
    public void integrationShouldWatchDeletingFolder() throws Exception {
        //given
        Observable<Node<?>> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribe(testSubscriber);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir/nestedMore"));
        delay(500l);

        FileUtils.forceDelete(new File(path + "/test1/nestedDir/nestedMore"));
        delay(500l);
        ///then
        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        Assert.assertThat(emittedEvents.size(), is(4));

        Assert.assertThat(emittedEvents.get(0).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(1).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(2).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(3).getType(), is(Type.ENTRY_DELETE));

    }

    @Test
    public void shouldTrackModified() throws Exception {
        //given

        Observable<Node<?>> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<Node<?>> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribe(testSubscriber);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));
        delay(500l);
        Files.move(Paths.get(path + "/test1"), Paths.get(path + "/test2/test1"), StandardCopyOption.REPLACE_EXISTING);
        delay(500l);

        ///then
        List<Node<?>> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        Assert.assertThat(emittedEvents.size(), is(4));

        Assert.assertThat(emittedEvents.get(0).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(1).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(2).getType(), is(Type.ENTRY_DELETE));
        Assert.assertThat(emittedEvents.get(3).getType(), is(Type.ENTRY_MODIFY));
    }
    private synchronized void delay(long l) throws InterruptedException {
        wait(l);
    }


}
