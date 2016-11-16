package com.challenge1.service;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ObserverServiceImplTest {

    @Test
    public void shouldCreateNotNullObservableFoeExistingPath() throws Exception{
        ObservableService observerService = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        Assert.assertThat(observableForPath, is(notNullValue()));
    }
    @Test
    public void shouldCreateNotNullObservableFoeNonExistingPath() throws Exception{
        ObservableService observerService = new ObserverServiceImpl();
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(null);
        Assert.assertThat(observableForPath, is(notNullValue()));
    }
    @Test
    public void shouldCreateObservableFormPath() throws Exception{
        ObservableService observerService = new ObserverServiceImpl();
        URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");
        Observable<Node<?>> observableForPath = observerService.getObservableForPath(Paths.get(parentFolder.toURI()));
        List<Node<?>> result = new ArrayList<>();
        observableForPath.subscribe(node ->result.add(node));

        Assert.assertThat(result.size(), is(7));
    }

}
