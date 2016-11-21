package com.challenge1.service.api;

import com.challenge1.ReadStreamApplicationTest;
import com.challenge1.service.watcher.*;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StructureWatcherTest {
    URL parentFolder = ReadStreamApplicationTest.class.getResource("/ParentFolder");


@Test
    public void test() throws Exception{
   // DirectoryRegistration directoryRegistration = new DirectoryRegistration();
 //   directoryRegistration.registerAll(Paths.get(parentFolder.toURI()));

    List<Node<?>> emittedList = new ArrayList<>();
    //StructureWatcher structureWatcher = new StructureWatcher(directoryRegistration,);



}

}