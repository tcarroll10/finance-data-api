package com.tcarroll10.finance.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tcarroll10.finance.domain.ErrorMsg;
import com.tcarroll10.finance.domain.Meta;
import com.tcarroll10.finance.domain.OutDataTo;
import com.tcarroll10.finance.repo.FinanceDataApiRepo;
import com.tcarroll10.finance.repo.FinanceMetaDataApiRepo;
import com.tcarroll10.finance.utils.Const;
import com.tcarroll10.finance.utils.ParamsMapUtil;

/**
 * Service for Finance data API service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

@Service
public class FinanceDataApiServiceImpl implements FinanceDataApiService {

  private static final Logger LOG = LogManager.getLogger(FinanceDataApiServiceImpl.class);

  private final FinanceDataApiRepo dataRepo;

  private final FinanceMetaDataApiRepo metaRepo;


  /**
   * Constructor for controller allows service injection.
   * 
   * @param dataRepo interface used for querying the datasource
   * @param metaRepo interface used for querying the metadata store
   * 
   */

  public FinanceDataApiServiceImpl(FinanceDataApiRepo dataRepo, FinanceMetaDataApiRepo metaRepo) {

    this.dataRepo = dataRepo;
    this.metaRepo = metaRepo;


  }

  @Override
  public ResponseEntity<?> validateRequestInput(String dataset, Map<String, String> paramsMap) {



    LOG.info("validateRequest with: {} dataset called and {} paramsmap", dataset, paramsMap);

    // check for valid dataset
    final Optional<ResponseEntity<?>> inValidDataset = validateDataset(dataset);
    if (inValidDataset.isPresent()) {

      return inValidDataset.get();
    }

    // check for valid keys
    final Optional<ResponseEntity<?>> inValidateParamsKeys = validateParamsKeys(paramsMap);
    if (inValidateParamsKeys.isPresent()) {

      return inValidateParamsKeys.get();

    }

    // if valid dataset and contains valid keys translate to sqlMap
    Map<String, String> sqlMap = translateParamsToSqlMap(paramsMap);
    //
    //
    // check for valid fields
    final Optional<ResponseEntity<?>> inValidateParamsFields =
        validateParamsFields(dataset, sqlMap);
    if (inValidateParamsFields.isPresent()) {

      return inValidateParamsFields.get();

    }

    // If validation passes, return a successful response with HTTP status
    List<Map<String, Object>> data = dataRepo.getData(dataset, paramsMap);
    Map<String, Map<String, String>> metaData = metaRepo.getMetaData(dataset);

    Meta meta = Meta.builder().totalCount(data.size()).dataFormats(metaData.get("dataFormats"))
        .labels(metaData.get("labels")).dataTypes(metaData.get("dataTypes")).build();

    OutDataTo output = OutDataTo.builder().data(data).meta(meta).build();

    return ResponseEntity.ok().body(output);

  }

  @Override
  public ResponseEntity<?> processRequestNoParams(String dataset) {



    // check for valid dataset
    final Optional<ResponseEntity<?>> response = validateDataset(dataset);
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
   * Validates path variable against datasets in metadata.
   * 
   * @param dataset interface used for querying the data source
   * @return optional is empty if valid
   * 
   */
  private Optional<ResponseEntity<?>> validateDataset(final String dataset) {

    Map<String, Map<String, String>> metaData = metaRepo.getMetaData(dataset);

    if (metaData.isEmpty()) {
      // If validation fails, return an error response with HTTP status
      // 400
      String msg = String.format("Path parameter localhost:8080/api/v2/%s "
          + "does not exist, for more information please see the documentation.", dataset);
      ErrorMsg error = ErrorMsg.builder().error(Const.BAD_DATASET).message(msg).build();
      return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }
    return Optional.empty();
  }

  /**
   * Validates that parameter keys are valid.
   * 
   * @param paramsMap interface used for querying the data source
   * @return optional is empty if valid
   */
  private Optional<ResponseEntity<?>> validateParamsKeys(final Map<String, String> paramsMap) {

    // check for valid keys in parameter map--must only contain fields,
    // filter, sort, etc

    String badKey = "";
    boolean isValid = true;
    for (String key : paramsMap.keySet()) {
      if (!Arrays.asList(Const.validKeys).contains(key)) {
        badKey = key;
        isValid = false;
        break;
      }

    }
    if (!isValid) {
      // If validation fails, return an error response with HTTP status
      // 400
      String msg = String.format(
          "Invalid query parameter '%s' with value '%s'. "
              + "For more information, please see the documentation.",
          badKey, paramsMap.get(badKey));
      ErrorMsg error = ErrorMsg.builder().error(Const.INVALD_QUERY_PARAMETER).message(msg).build();
      return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    return Optional.empty();

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
    sqlMap.put("filters", ParamsMapUtil.processFilters(paramsMap));

    return sqlMap;



  }

  /**
   * Validates path variable against datasets in metadata.
   * 
   * @param dataset interface used for querying the data source
   * @return optional is empty if valid
   * 
   */
  private Optional<ResponseEntity<?>> validateParamsFields(final String dataset,
      final Map<String, String> sqlMap) {


    Map<String, String> metadata = metaRepo.getMetaData(dataset).get("labels");

    // validate
    if (!sqlMap.containsKey("fields"))
      return Optional.empty();

    String values = sqlMap.get("fields");

    if (values != null && !values.isEmpty()) {

      String[] fields = sqlMap.get("fields").split(",");

      for (String field : fields) {
        if (!metadata.containsKey(field)) {


          String msg = String.format("Invalid query parameter '%s' with value '%s'. "
              + "For more information, please see the documentation.", "field", field);
          ErrorMsg error =
              ErrorMsg.builder().error(Const.INVALD_QUERY_PARAMETER).message(msg).build();
          return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));

        }


      }

    }



    return Optional.empty();

  }


}
