package com.challenge1.service.api;

import com.challenge1.service.PathContext;
import rx.Observable;

import java.nio.file.Path;

public interface ObservableService {
    Observable<PathContext> getObservableForPath(Path path);
    Observable<PathContext> getDirectoryWatcherObservable(Path path);
}

