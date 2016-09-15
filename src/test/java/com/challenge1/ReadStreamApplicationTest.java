package com.challenge1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReadStreamApplicationTest {

	@Test
	public void contextLoads() {
		//spring boot tests with reactive doesn't work for some reason - added integration test marker solve this issue.
		//https://github.com/spring-projects/spring-boot/issues/4908
	}

}
