package com.tcarroll10.findata.repo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class FindataMetadataApiRepoFileImpl extends FindataMetadataAbstractFileDao
    implements FindataMetadataApiRepo {

  private final ObjectMapper objectMapper;

  public FindataMetadataApiRepoFileImpl(@Value("${file.path.name:metadata.json}") String filePath,
      ObjectMapper objectMapper) {
    super(filePath);
    this.objectMapper = objectMapper;
  }

  @Override
  public Map<String, Map<String, String>> getMetaData(String dataset,
      Map<String, String> paramsMap) {


    if (!paramsMap.containsKey("fields") || paramsMap.get("fields") == null
        || paramsMap.get("fields").isEmpty()) {
      return getMetaData(dataset);
    }



    String[] fieldsArray =
        Arrays.stream(paramsMap.get("fields").split(",")).map(String::trim).toArray(String[]::new);

    Map<String, Map<String, String>> result = new HashMap<>();

    try {
      JsonNode rootNode = objectMapper.readTree(jsonData);

      if (rootNode.has(dataset)) {
        JsonNode topLevelNode = rootNode.get(dataset);

        Map<String, String> labels = extractNodeValues(topLevelNode, "labels", fieldsArray);
        Map<String, String> dataTypes = extractNodeValues(topLevelNode, "dataTypes", fieldsArray);
        Map<String, String> dataFormats =
            extractNodeValues(topLevelNode, "dataFormats", fieldsArray);

        result.put("labels", labels);
        result.put("dataTypes", dataTypes);
        result.put("dataFormats", dataFormats);

      } else {

        System.err.println("Dataset Metdata not found: " + dataset);
        return null;
      }
    } catch (IOException e) {

      e.printStackTrace();
      return null;
    }
    return result;
  }

  private static Map<String, String> extractNodeValues(JsonNode node, String subNodeName,
      String[] fields) {
    Map<String, String> result = new HashMap<>();

    if (node.has(subNodeName)) {
      JsonNode subNode = node.get(subNodeName);

      for (String field : fields) {
        if (subNode.has(field)) {
          result.put(field, subNode.get(field).asText());
        } else {
          // Handle the case where the field is not found in the
          // subNode
          System.err.println("Field not found: " + field);
        }
      }
    }

    return result;
  }

  @Override
  public Map<String, Map<String, String>> getMetaData(String dataset) {
    Map<String, Map<String, String>> result = new HashMap<>();

    try {
      JsonNode rootNode = objectMapper.readTree(jsonData);

      if (rootNode.has(dataset)) {
        JsonNode topLevelNode = rootNode.get(dataset);

        result.put("labels", convertJsonNodeToMap(topLevelNode.get("labels")));
        result.put("dataTypes", convertJsonNodeToMap(topLevelNode.get("dataTypes")));
        result.put("dataFormats", convertJsonNodeToMap(topLevelNode.get("dataFormats")));

      } else {
        System.err.println("Dataset Metadata not found: " + dataset);
        return new HashMap<String, Map<String, String>>();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return result;
  }

  private Map<String, String> convertJsonNodeToMap(JsonNode node) {
    Map<String, String> resultMap = new HashMap<>();

    if (node != null && node.isObject()) {
      node.fields()
          .forEachRemaining(entry -> resultMap.put(entry.getKey(), entry.getValue().asText()));
    }

    return resultMap;
  }

}
