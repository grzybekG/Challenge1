package com.challenge1.service;

import com.challenge1.service.api.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mlgy on 2016-09-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceImplTest {

    public static String TEST_RESOURCE_PATH = "src/test/resources/ParentFolder";
    private FileService fileService = new FileServiceImpl();
    @Test
    public void convertTargetToIterableTest(){


}
}