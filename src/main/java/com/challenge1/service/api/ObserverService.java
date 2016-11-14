package com.challenge1.service.api;


import rx.Observable;

import java.nio.file.Path;

public interface ObserverService {
    Observable<Node<?>> getObservableForPath(Path path);
}
