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
import rx.schedulers.Schedulers;

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
        Observable<PathContext> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateNotNullObservableFoeNonExistingPath() throws Exception {
        Observable<PathContext> observableForPath = observerService.getObservableForPath(null);
        Assert.assertThat(observableForPath, is(notNullValue()));
    }

    @Test
    public void shouldCreateObservableFormPath() throws Exception {
        Observable<PathContext> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        List<PathContext> result = new ArrayList<>();
        observableForPath.subscribe(node -> result.add(node));

        Assert.assertThat(result.size(), is(7));
    }

    @Test //
    public void integrationTestSimpleFolderChange() throws Exception {
        //given
        Observable<PathContext> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));

        TestSubscriber<PathContext> testSubscriber = new TestSubscriber();
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(testSubscriber);
        Thread.sleep(500l);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));

        //then
        List<PathContext> emittedEvents = testSubscriber.getOnNextEvents();


        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(5500l, TimeUnit.MICROSECONDS);
        Assert.assertThat(emittedEvents.size(), is(2));
        for (PathContext context : emittedEvents) {
            Assert.assertThat(context.getType(), is(Type.ENTRY_CREATE));
        }
    }

    @Test
    public void integrationTestNestedFolders() throws Exception {
        //given
        Observable<PathContext> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<PathContext> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(testSubscriber);
        Thread.sleep(500l);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir"));

        ///then
        Thread.sleep(200l);

        List<PathContext> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();


        //TODO check emitted events are one you expected
        Assert.assertThat(emittedEvents.size(), is(2));

    }

    @Test
    public void integrationShouldWatchDeletingFolder() throws Exception {
        //given
        Observable<PathContext> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<PathContext> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(testSubscriber);
        Thread.sleep(200l);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir"));
        Files.createDirectory(Paths.get(path + "/test1/nestedDir/nestedMore"));


        Thread.sleep(200l);
        FileUtils.forceDelete(new File(path + "/test1/nestedDir/nestedMore"));
        Thread.sleep(200l);
        ///then
        List<PathContext> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        Assert.assertThat(emittedEvents.size(), is(4));

        Assert.assertThat(emittedEvents.get(0).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(1).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(2).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(3).getType(), is(Type.ENTRY_DELETE));
        //cleanup

    }

    @Test
    public void shouldTrackModified() throws Exception {
        //given
        Observable<PathContext> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<PathContext> testSubscriber = new TestSubscriber<>();
        //when
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(testSubscriber);

        Thread.sleep(1000l);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));
        Files.move(Paths.get(path + "/test1"), Paths.get(path + "/test2/test1"), StandardCopyOption.REPLACE_EXISTING);
        Thread.sleep(200l);
        ///then
        List<PathContext> emittedEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        Assert.assertThat(emittedEvents.size(), is(4));

        Assert.assertThat(emittedEvents.get(0).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(1).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(emittedEvents.get(2).getType(), is(Type.ENTRY_MODIFY));
        Assert.assertThat(emittedEvents.get(3).getType(), is(Type.ENTRY_DELETE));
    }

    @Test
    public void multipleSubscriptions() throws Exception {
        //given
        Observable<PathContext> directoryWatcherObservable = observerService.getDirectoryWatcherObservable(Paths.get(parentFolder.toURI()));
        TestSubscriber<PathContext> testSubscriber = new TestSubscriber<>();
        TestSubscriber<PathContext> firstSubscriber = testSubscriber.create();
        TestSubscriber<PathContext> secondSubscriber = testSubscriber.create();

        //when
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(firstSubscriber);
        directoryWatcherObservable.subscribeOn(Schedulers.io()).subscribe(secondSubscriber);
        Thread.sleep(500l);
        Files.createDirectory(Paths.get(path + "/test1"));
        Files.createDirectory(Paths.get(path + "/test2"));
        Thread.sleep(500l);
        Files.move(Paths.get(path + "/test1"), Paths.get(path + "/test2/test1"), StandardCopyOption.REPLACE_EXISTING);
        Thread.sleep(500l);

        ///then
        List<PathContext> firstSubscriberOnNextEvents = firstSubscriber.getOnNextEvents();
        List<PathContext> secondSubscriberOnNextEvents = secondSubscriber.getOnNextEvents();
        firstSubscriber.assertNoErrors();
        secondSubscriber.assertNoErrors();

        Assert.assertThat(firstSubscriberOnNextEvents.size(), is(4));
        Assert.assertThat(secondSubscriberOnNextEvents.size(), is(4));

        Assert.assertThat(firstSubscriberOnNextEvents.get(0).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(firstSubscriberOnNextEvents.get(1).getType(), is(Type.ENTRY_CREATE));
        Assert.assertThat(firstSubscriberOnNextEvents.get(2).getType(), is(Type.ENTRY_DELETE));
        Assert.assertThat(firstSubscriberOnNextEvents.get(3).getType(), is(Type.ENTRY_MODIFY));
    }


}
