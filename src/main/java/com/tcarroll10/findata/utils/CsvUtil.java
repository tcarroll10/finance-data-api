package com.tcarroll10.findata.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvUtil {

  public static String convertListMapToCsv(List<Map<String, Object>> data) {

    if (data == null || data.isEmpty()) {
      return ""; // No data to process
    }
    StringBuilder csvBuilder = new StringBuilder();

    // Get the headers from the keys of the first row (assuming all rows have the same structure)
    Set<String> headers = data.get(0).keySet();
    csvBuilder.append(String.join(",", headers));
    csvBuilder.append("\n");

    // Iterate through each map (row) and convert values to a CSV row
    for (Map<String, Object> row : data) {
      String rowCsv = headers.stream().map(header -> row.getOrDefault(header, "").toString())
          .collect(Collectors.joining(",")); // Join values with commas
      csvBuilder.append(rowCsv);
      csvBuilder.append("\n");
    }

    return csvBuilder.toString();
  }



}
