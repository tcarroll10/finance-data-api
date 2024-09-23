package com.tcarroll10.findata.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tcarroll10.findata.domain.Meta;
import com.tcarroll10.findata.domain.OutDataTo;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.repo.FindataMetadataApiRepo;
import com.tcarroll10.findata.utils.CsvUtil;
import com.tcarroll10.findata.utils.ParamsMapUtil;

/**
 * Service for Finance data API service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

@Service
public class FindataApiServiceImpl implements FindataApiService {

  private static final Logger LOG = LogManager.getLogger(FindataApiServiceImpl.class);

  private final FindataApiRepo dataRepo;

  private final FindataMetadataApiRepo metaRepo;

  private final FindataValidatorServiceImpl validator;



  /**
   * Constructor for controller allows service injection.
   * 
   * @param dataRepo interface used for querying the datasource
   * @param metaRepo interface used for querying the metadata store
   * 
   */

  public FindataApiServiceImpl(FindataApiRepo dataRepo, FindataMetadataApiRepo metaRepo,
      FindataValidatorServiceImpl validator) {

    this.dataRepo = dataRepo;
    this.metaRepo = metaRepo;
    this.validator = validator;

  }

  @Override
  public ResponseEntity<?> validateRequestInput(String dataset, Map<String, String> paramsMap) {

    LOG.info("validateRequest with: {} dataset called and {} paramsmap", dataset, paramsMap);

    // check for valid dataset
    final Optional<ResponseEntity<?>> inValidDataset = validator.validateDataset(dataset);
    if (inValidDataset.isPresent()) {

      return inValidDataset.get();
    }

    // check for valid keys
    final Optional<ResponseEntity<?>> inValidateParamsKeys =
        validator.validateParamsKeys(paramsMap);
    if (inValidateParamsKeys.isPresent()) {

      return inValidateParamsKeys.get();

    }

    // if valid dataset and contains valid keys translate to sqlMap
    final Map<String, String> sqlMap = translateParamsToSqlMap(paramsMap);

    // check for valid fields
    final Optional<ResponseEntity<?>> validateFields = validator.validateFields(dataset, sqlMap);
    LOG.info("validate fields called with with: {} dataset called and {} sqlmap", dataset, sqlMap);
    if (validateFields.isPresent()) {

      return validateFields.get();

    }

    // check for valid format
    final Optional<ResponseEntity<?>> validateFormat = validator.validateFormat(dataset, sqlMap);
    LOG.info("validate format called with with: {} dataset called and {} sqlmap", dataset, sqlMap);
    if (validateFormat.isPresent()) {

      return validateFormat.get();

    }

    // If validation passes, pull data
    List<Map<String, Object>> data = dataRepo.getData(dataset, sqlMap);


    // Determine the format: JSON or CSV
    String format = sqlMap.getOrDefault("format", "json");

    if ("csv".equalsIgnoreCase(format)) {
      String csvData = CsvUtil.convertListMapToCsv(data);

      return ResponseEntity.ok().header("Content-Type", "text/csv").body(csvData);

    } else {

      Map<String, Map<String, String>> metaData = metaRepo.getMetaData(dataset);

      Meta meta = Meta.builder().totalCount(data.size()).dataFormats(metaData.get("dataFormats"))
          .labels(metaData.get("labels")).dataTypes(metaData.get("dataTypes")).build();

      OutDataTo output = OutDataTo.builder().data(data).meta(meta).build();

      return ResponseEntity.ok().body(output);
    }


  }



  @Override
  public ResponseEntity<?> processRequestNoParams(String dataset) {



    // check for valid dataset
    final Optional<ResponseEntity<?>> response = validator.validateDataset(dataset);
    if (response.isPresent()) {

      return response.get();
    }

    List<Map<String, Object>> data = dataRepo.getData(dataset);


    Map<String, Map<String, String>> metaData = metaRepo.getMetaData(dataset);
    Meta meta = Meta.builder().totalCount(data.size()).dataFormats(metaData.get("dataFormats"))
        .labels(metaData.get("labels")).dataTypes(metaData.get("dataTypes")).build();

    OutDataTo output = OutDataTo.builder().data(data).meta(meta).build();

    return ResponseEntity.ok().body(output);
  }



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
    sqlMap.put("filter", ParamsMapUtil.processFilters(paramsMap));
    sqlMap.put("sort", ParamsMapUtil.processSort(paramsMap));
    sqlMap.put("per_page", paramsMap.getOrDefault("per_page", "100"));
    sqlMap.put("page", ParamsMapUtil.processPageNumber(paramsMap));
    sqlMap.put("format", paramsMap.getOrDefault("format", "json"));


    return sqlMap;



  }



}
