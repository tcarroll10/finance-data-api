package com.tcarroll10.findata.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParamsMapUtil class for Finance data API service. Provides methods for processing the request
 * parameter strings.
 * 
 * @author tom carroll
 * @since 2023-12-27
 */


public class ParamsMapUtil {

  /**
   * Extracts the fields request parameter. If the fields parameter is not present or empty will
   * return "". If present, will return a comma separated list of the fields with whitespace
   * trimmed.
   * 
   * @param paramsMap map of parameters
   * @return string of fields
   */

  public static String processFields(Map<String, String> paramsMap) {

    String result = "*";

    if (!paramsMap.containsKey("fields"))
      return result;

    String values = paramsMap.get("fields");



    if (values != null && !values.isEmpty()) {

      String[] fieldsArray =
          Arrays.stream(values.split(",")).map(String::trim).toArray(String[]::new);

      String selectFields = String.join(", ", fieldsArray);

      result = selectFields;
    }

    return result;

  }



  /**
   * Processes the 'filter' request parameter. If the parameter is not present or empty will return
   * empty string. If present, will return a comma separated list of the fields with whitespace
   * trimmed.
   * 
   * @param paramsMap map of parameters
   * @return the where portion of the sql statement
   */

  public static String processFilters(Map<String, String> paramsMap) {

    String sqlWhere = "";
    List<String> pieces = new ArrayList<>();

    if (!paramsMap.containsKey("filter"))
      return sqlWhere;

    String values = paramsMap.get("filter");

    if (values != null && !values.isEmpty()) {

      String[] filterArray = splitFilterParam(values);

      for (String item : filterArray) {

        String[] split = item.split(":");

        if (!split[1].equals("in")) {
          pieces.add(split[0] + " " + OperatorMapper.mapper(split[1]) + " " + "'" + split[2] + "'");
        } else {


          List<String> result1 = new ArrayList<>();
          String modifiedString = split[2].substring(1, split[2].length() - 1);

          String[] inList = modifiedString.split(",");

          for (String entry : inList) {
            result1.add(String.format("'%s'", entry));
          }

          String inListItems = String.join(",", result1);

          pieces.add(String.format("%s in (%s)", split[0], inListItems));

        }

      }

      sqlWhere = String.join(" and ", pieces);

    }

    return sqlWhere;

  }

  /**
   * Processes the 'sort' request parameter. If the parameter is not present or empty will return
   * empty string.
   * 
   * @param paramsMap map of parameters
   * @return the order by portion of the sql statement
   */

  public static String processSort(Map<String, String> params) {

    String orderBy = "";
    List<String> pieces = new ArrayList<>();

    if (!params.containsKey("sort"))
      return orderBy;

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

      orderBy = String.join(", ", pieces);


    }

    return orderBy;
  }

  /**
   * Processes the 'page[number]' request parameter.
   * 
   * @param paramsMap map of parameters
   * @return the OFFSET portion of the sql statement
   */



  public static String processPageNumber(Map<String, String> paramsMap) {
    if (!paramsMap.containsKey("page")) {
      return "0";
    }

    // Convert 'page[number]' and 'page[size]' to integers
    int pageNumber = Integer.parseInt(paramsMap.get("page"));
    int pageSize = Integer.parseInt(paramsMap.get("per_page"));

    // Calculate the offset
    int offset = (pageNumber - 1) * pageSize;

    // Return the offset as a String
    return String.valueOf(offset);
  }



  public static String generateSql(String dataset, Map<String, String> sqlMap) {


    String select = sqlMap.getOrDefault("fields", "");

    String from = dataset;

    String where = sqlMap.getOrDefault("filter", "");

    String orderBy = sqlMap.getOrDefault("sort", "");

    String pageSize = sqlMap.getOrDefault("per_page", "100");

    String pageNumber = sqlMap.getOrDefault("page", "0");



    StringBuilder resultBuilder = new StringBuilder();

    if (!select.trim().isEmpty()) {
      resultBuilder.append("SELECT " + select.trim()).append("\n");
    }

    if (!from.trim().isEmpty()) {
      resultBuilder.append("FROM " + from.trim()).append("\n");
    }

    if (!where.trim().isEmpty()) {
      resultBuilder.append("WHERE " + where.trim()).append("\n");
    }

    if (!orderBy.trim().isEmpty()) {
      resultBuilder.append("ORDER By " + orderBy.trim()).append("\n");
    }

    resultBuilder.append("LIMIT " + pageSize.trim()).append("\n");

    resultBuilder.append("OFFSET " + pageNumber.trim());


    return resultBuilder.toString();

  }

  /**
   * Splits the 'filter' request parameter. Uses a
   * 
   * @param input string to split
   * @return a filter
   */
  private static String[] splitFilterParam(String input) {

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
