package com.tcarroll10.findata.controller;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tcarroll10.findata.service.FinDataApiService;

/**
 * RestController class for Finance data api service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

@RequestMapping(path = "/api/v2", produces = "application/json")

@RestController
public class FinDataApiController {

  private static final Logger LOG = LogManager.getLogger(FinDataApiController.class);

  private final FinDataApiService service;

  /**
   * Constructor for controller allows service injection.
   * 
   * @param service Command-line arguments.
   */

  public FinDataApiController(FinDataApiService service) {

    this.service = service;
  }

  /**
   * Primary controller method.
   * 
   * @param dataset is name of table or dataset used in request.
   * @param paramsMap is name of Map of request parameters. Comes in form of "parameter=value".
   *        e.g., ?fields=record_date
   * @return returns a response entity with either data or an error message
   */

  @GetMapping("/{dataset}")
  public ResponseEntity<?> getData(@PathVariable("dataset") String dataset,
      @RequestParam(required = false) Map<String, String> paramsMap) {

    LOG.info("dataset: {}", dataset);
    LOG.info("params: {}", paramsMap);

    if (paramsMap != null && !paramsMap.isEmpty()) {

      return service.validateRequestInput(dataset, paramsMap);
    } else {

      return service.processRequestNoParams(dataset);
    }

  }

}
