package com.challenge1.service;

import com.challenge1.service.api.Node;
import org.junit.Test;
import rx.Observable;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mlgy on 20/10/2016.
 */
public class FileServiceImplTest {

    @Test
    public void firstTest(){
        FileServiceImpl impl =  new FileServiceImpl();
        Observable<Node<Path>> observableFor = impl.getObservableFor(Paths.get("c:/dev"));

        assert(true);

    }

}