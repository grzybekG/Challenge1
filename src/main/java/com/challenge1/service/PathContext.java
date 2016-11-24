package com.challenge1.service;

import com.challenge1.service.api.Type;

import java.nio.file.Path;

public class PathContext {
    Path path;
    Type type;

    public PathContext(Path path, Type type) {
        this.path = path;
        this.type = type;
    }

    public Path getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }
}

