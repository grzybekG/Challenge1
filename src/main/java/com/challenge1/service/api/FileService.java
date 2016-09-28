package com.challenge1.service.api;

import com.google.common.collect.Iterables;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public interface FileService {

    Iterator<Leaf> convertTargetToIterable(String from);

    String withMapSolution(Path path);

}
