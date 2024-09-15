package com.tcarroll10.finance.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.tcarroll10.findata.controller.FindataApiController;

@WebMvcTest(FindataApiController.class)
@ContextConfiguration(classes = {FindataApiController.class})

public class WebLayerTest {

  @Autowired
  private MockMvc mockMvc;


  // @Test
  public void testGetEndpoint() throws Exception {

    mockMvc.perform(get("/api/v2/Security")).andExpect(status().isOk());
  }


}
