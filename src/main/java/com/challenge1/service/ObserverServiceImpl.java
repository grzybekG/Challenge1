package com.challenge1.service;

import com.challenge1.service.api.Node;
import com.challenge1.service.api.ObservableService;
import com.challenge1.service.watcher.StructureWatcher;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.nio.file.Path;


@Service
public class ObserverServiceImpl implements ObservableService {


    @Override
    public Observable<Node<?>> getObservableForPath(Path path) {
        return Observable.from(NodeLogic.getNodeIterator(new FileHandlerImpl(path)));
    }

    @Override
    public Observable<Node<?>> getDirectoryWatcherObservable(Path path) {
        Observable<Node<?>> pathObservable = Observable.create(subscriber -> {
            StructureWatcher watcher = new StructureWatcher(path, node -> subscriber.onNext(node));
            Thread directoryWatcher = new Thread(watcher);
            directoryWatcher.start();
        });
        return pathObservable;
    }
}
