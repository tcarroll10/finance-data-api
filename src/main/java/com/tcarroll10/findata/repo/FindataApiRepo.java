package com.tcarroll10.findata.repo;

import java.util.List;
import java.util.Map;

/**
 * Repo interface for Finance data api service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

public interface FindataApiRepo {

  /**
   * This method supports requests with no parameters.
   * 
   * @param dataset is the table or dataset to query.
   * @return a List of Maps representing the query resutls
   */

  public List<Map<String, Object>> getData(String dataset);

  /**
   * This method supports requests with parameters such as fields, filters, sort.
   * 
   * @param dataset is the table or dataset to query.
   * @param paramsMap the parameters for the query
   * @return a List of Maps representing the query results
   */

  public List<Map<String, Object>> getData(String dataset, Map<String, String> sqlMap);



}
