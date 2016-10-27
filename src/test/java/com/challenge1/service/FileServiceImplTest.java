package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;

import java.nio.file.Path;
import java.nio.file.Paths;


public class FileServiceImplTest {

    @Test
    public void firstTest(){
        FileServiceImpl impl =  new FileServiceImpl();
        Observable<Node<Path>> observableFor = impl.getObservableFor(Paths.get("c:/dev"));

        Assert.assertTrue(observableFor != null);

    }

}