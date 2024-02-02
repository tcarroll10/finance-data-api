package com.tcarroll10.findata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParamsMapHelper class for Finance data api service. Provides methods for processing the request
 * parameter strings.
 * 
 * @author tom carroll
 * @since 2023-12-27
 */

public abstract class ParamsMapHelper {

  /**
   * Processes the fields request parameter. If the fields parameter is not present or empty will
   * return 'select *'. If present, will return a comma separated list of the fields with whitespace
   * trimmed.
   * 
   * @param paramsMap map of parameters
   * @return the select portion of the sql statement
   */

  public static String processFields(Map<String, String> paramsMap) {

    String sql = "SELECT *";

    if (!paramsMap.containsKey("fields"))
      return sql;

    String values = paramsMap.get("fields");

    if (values != null && !values.isEmpty()) {

      String[] fieldsArray =
          Arrays.stream(values.split(",")).map(String::trim).toArray(String[]::new);

      String selectFields = String.join(", ", fieldsArray);

      sql = "SELECT " + selectFields;
    }

    return sql;

  }

  /**
   * Processes the 'filter' request parameter. If the parameter is not present or empty will return
   * empty string. If present, will return a comma separated list of the fields with whitespace
   * trimmed.
   * 
   * @param paramsMap map of parameters
   * @return the select portion of the sql statement
   */

  public static String processFilters(Map<String, String> paramsMap) {

    String sql = "";
    List<String> pieces = new ArrayList<>();

    if (!paramsMap.containsKey("filter"))
      return sql;

    String values = paramsMap.get("filter");

    if (values != null && !values.isEmpty()) {

      String[] filterArray = splitFilterParam(values);

      for (String item : filterArray) {

        String[] split = item.split(":");

        if (!split[1].equals("in")) {
          pieces.add(split[0] + " " + OperatorMapper.mapper(split[1]) + " " + "'" + split[2] + "'");
        } else {

          String result = "";
          List<String> result1 = new ArrayList<>();
          String modifiedString = split[2].substring(1, split[2].length() - 1);

          String[] inList = modifiedString.split(",");

          for (String entry : inList) {
            result += String.format("'%s'", entry);
            result1.add(String.format("'%s'", entry));

          }

          String inListItems = String.join(",", result1);
          // return String.format("WHERE %s in (%s)", split[0],
          // result);
          return String.format("WHERE %s in (%s)", split[0], inListItems);

        }

      }

      String sqlWhere = String.join(" and ", pieces);

      return "WHERE " + sqlWhere;

    }

    return sql;

  }

  public static String processSort(Map<String, String> params) {

    String sql = "";
    List<String> pieces = new ArrayList<>();

    if (!params.containsKey("sort"))
      return sql;

    String values = params.get("sort");

    if (values != null && !values.isEmpty()) {

      String[] sortArray = values.split(",");

      for (String item : sortArray) {

        if (item.charAt(0) == '-') {
          pieces.add(item.substring(1) + " DESC");
        } else {
          pieces.add(item + " ASC");
        }

      }

      String orderByClause = String.join(", ", pieces);

      return "ORDER BY " + orderByClause;
    }

    return sql;
  }

  public static String processParamsMapToSql(String dataset, Map<String, String> paramsMap) {

    String fields = "";
    String filter = "";
    String sort = "";
    String from = "FROM " + dataset;

    String processedFields = processFields(paramsMap);
    if (processedFields != null) {
      fields = processedFields;
    }

    String processedFilters = processFilters(paramsMap);
    if (processedFilters != null) {
      filter = processedFilters;
    }

    String processedSort = processSort(paramsMap);
    if (processedSort != null) {
      sort = processedSort;
    }

    StringBuilder resultBuilder = new StringBuilder();

    if (!fields.trim().isEmpty()) {
      resultBuilder.append(fields.trim()).append("\n");
    }

    if (!from.trim().isEmpty()) {
      resultBuilder.append(from.trim());
    }

    if (!filter.trim().isEmpty()) {
      resultBuilder.append("\n").append(filter.trim());
    }

    if (!sort.trim().isEmpty()) {
      resultBuilder.append("\n").append(sort.trim());
    }

    return resultBuilder.toString();

  }

  public static String[] splitFilterParam(String input) {

    Pattern pattern = Pattern.compile("(?:,?([^:]+:[^:]+:\\([^)]+\\)))|(?:,?([^:]+:[^:]+:[^,]+))");
    Matcher matcher = pattern.matcher(input);

    // Create a list to store the results
    List<String> result = new ArrayList<>();

    // Find all matches
    while (matcher.find()) {
      // Choose the non-null group based on the matched item type
      result.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2));
    }

    // Convert the list to an array and trim each element
    return result.stream().map(String::trim).toArray(String[]::new);
  }

}
