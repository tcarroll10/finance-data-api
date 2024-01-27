package com.tcarroll10.finance.service;

import java.util.Arrays;
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

    // If validation passes, return a successful response with HTTP status
    List<Map<String, Object>> data = dataRepo.getData(dataset, paramsMap);
    Map<String, Map<String, String>> metaData = metaRepo.getMetaData(dataset, paramsMap);
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

    Map<String, Map<String, String>> metadata = metaRepo.getMetaData(dataset);
    if (metadata.isEmpty()) {
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
}
