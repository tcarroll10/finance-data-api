package com.tcarroll10.finance.utils;

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


public abstract class ParamsMapUtil {

  /**
   * Extracts the fields request parameter. If the fields parameter is not present or empty will
   * return "". If present, will return a comma separated list of the fields with whitespace
   * trimmed.
   * 
   * @param paramsMap map of parameters
   * @return string of fields
   */

  public static String processFields(Map<String, String> paramsMap) {

    String result = "";

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
   * @return the select portion of the sql statement
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
   * Splits the 'filter' request parameter. Uses a
   * 
   * @param input string to split
   * @return a filter
   */
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
