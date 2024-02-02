package com.tcarroll10.findata.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * Service for processing the fin data api request
 * 
 * @author tom carroll
 * @version 2023-12-27
 */
public interface FindataApiService {

  /**
   * This method will validate the user request parameter inputs and return ResponseEntity with
   * either the data and metadata or an error message.
   * 
   * @param dataset is the table from which client wants data.
   * @param paramsMap holds the request parameters as keys and their values.
   * @return a ResponseEntity with the results and metadata results.
   */

  public ResponseEntity<?> validateRequestInput(String dataset, Map<String, String> paramsMap);

  /**
   * This method will return response entity with data and metadata. .
   * 
   * @param dataset is the table from which client wants data.
   * @return a ResponseEntity with the results and metadata results.
   */

  public ResponseEntity<?> processRequestNoParams(String dataset);

}
