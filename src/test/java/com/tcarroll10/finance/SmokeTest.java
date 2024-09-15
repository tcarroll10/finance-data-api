package com.tcarroll10.finance;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import com.tcarroll10.findata.FindataApiApplication;


@SpringBootTest(classes = FindataApiApplication.class)
public class SmokeTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testContextLoads() {

    assertNotNull(applicationContext);
  }



}
