package com.tcarroll10.findata.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.tcarroll10.findata.utils.ParamsMapUtil;

@Service
public class FindataParamsTranslator {

  /**
   * Translates the parameter map into a map where values are strings that we can use to build sql
   * statements
   * 
   * @param paramsMap holds the request parameters from client
   * @return
   * @return valid map is empty if valid
   */
  private Map<String, String> translateParamsToSqlMap(final Map<String, String> paramsMap) {
    Map<String, String> sqlMap = new HashMap<>();

    sqlMap.put("fields", ParamsMapUtil.processFields(paramsMap));
    sqlMap.put("filters", ParamsMapUtil.processFilters(paramsMap));
    // add sort, pagination, and format


    return sqlMap;



  }

}
