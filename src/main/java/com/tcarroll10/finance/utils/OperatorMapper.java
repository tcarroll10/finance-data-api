package com.tcarroll10.finance.utils;

import java.util.HashMap;
import java.util.Map;

public class OperatorMapper {

  private static final Map<String, String> operatorMapper = new HashMap<>();

  static {
    operatorMapper.put("eq", "=");
    operatorMapper.put("lt", "<");
    operatorMapper.put("lte", "<=");
    operatorMapper.put("gt", ">");
    operatorMapper.put("gte", ">=");
    operatorMapper.put("in", "in");

  }

  public static String mapper(String operator) {

    return operatorMapper.getOrDefault(operator, "=");
  }

}
