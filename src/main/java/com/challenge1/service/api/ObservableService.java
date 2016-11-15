package com.challenge1.service.api;

import rx.Observable;

import java.nio.file.Path;

/**
 * Created by mlgy on 20/10/2016.
 */
public interface ObservableService {
    Observable<Node<?>> getObservableForPath(Path path);
    Observable<Node<?>> getDirectoryWatcherObservable(Path path);
}
