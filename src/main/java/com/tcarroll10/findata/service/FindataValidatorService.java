package com.tcarroll10.findata.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface FindataValidatorService {

  public Optional<ResponseEntity<?>> validateDataset(final String dataset);

  public Optional<ResponseEntity<?>> validateParamsKeys(final Map<String, String> paramsMap);

  public Optional<ResponseEntity<?>> validateFields(final String dataset,
      final Map<String, String> sqlMap);

  public Optional<ResponseEntity<?>> validateFormat(final String dataset,
      final Map<String, String> sqlMap);

}
