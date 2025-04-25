package com.example.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author cxj
 * @Date 2025/4/21 21:22
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test() {
        System.out.println("test1");
        System.out.println("test2");
        System.out.println("test3");
    }
}
