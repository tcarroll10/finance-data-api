package com.tcarroll10.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.tcarroll10.findata.FindataApiApplication;

/*
 * basic test to ensure that the Spring Application Context loads without any issues.
 * 
 */


@SpringBootTest(classes = FindataApiApplication.class)
class FindataApiApplicationTest {

  @Test
  void contextLoads() {}

}
