package com.tcarroll10.findata.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tcarroll10.findata.domain.ErrorMsg;
import com.tcarroll10.findata.repo.FinMetaDataApiRepo;
import com.tcarroll10.findata.utils.Const;

@Service
public class FinDataValidatorService {



  private static final Logger LOG = LogManager.getLogger(FinDataValidatorService.class);

  private final FinMetaDataApiRepo metaRepo;

  /**
   * Constructor for controller allows service injection.
   * 
   * 
   * @param metaRepo interface used for querying the metadata store
   * 
   */


  public FinDataValidatorService(FinMetaDataApiRepo metaRepo) {

    this.metaRepo = metaRepo;

  }

  /**
   * Validates path variable against datasets in metadata.
   * 
   * @param dataset interface used for querying the data source
   * @return optional is empty if valid
   * 
   */
  public Optional<ResponseEntity<?>> validateDataset(final String dataset) {

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
  public Optional<ResponseEntity<?>> validateParamsKeys(final Map<String, String> paramsMap) {

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
   * Validates path variable against datasets in metadata.
   * 
   * @param dataset interface used for querying the data source
   * @return optional is empty if valid
   * 
   */
  public Optional<ResponseEntity<?>> validateParamsFields(final String dataset,
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


    // add code to validate filter parameter



    return Optional.empty();

  }

}
