package com.challenge1.service.watcher;

import com.challenge1.service.NodeLogic;
import com.challenge1.service.api.FileModificationListener;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

import static org.mockito.Mockito.spy;


public class DirectoryRegistrationTest {
    private Path path = Paths.get("mock");
    private WatchService watchService = Mockito.mock(WatchService.class);
    private FileModificationListener listener = spy(FileModificationListener.class);
    private NodeLogic nodeLogicMock = spy(NodeLogic.class);

    @Test
    public void firstTest() {
        DirectoryRegistration directoryRegistration = new DirectoryRegistration(watchService,nodeLogicMock, listener);
        directoryRegistration.registerAll(path);
        ReflectionTestUtils.setField(directoryRegistration, "nodeLogic", nodeLogicMock);


    }

}