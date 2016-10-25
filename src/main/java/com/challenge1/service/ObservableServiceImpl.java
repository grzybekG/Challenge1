package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import rx.Observable;

import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by mlgy on 20/10/2016.
 */
public class ObservableServiceImpl implements ObservableService {

    public static Observable<Node<Path>> getObservable(Iterable<Node<Path>> nodeIterable) {

        return Observable.create(subscriber -> {
            Iterator<Node<Path>> iterator = nodeIterable.iterator();
            while (iterator.hasNext()){
                subscriber.onNext(iterator.next());
            }

        });
    }
}