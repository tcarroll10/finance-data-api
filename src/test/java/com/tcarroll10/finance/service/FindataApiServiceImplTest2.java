package com.tcarroll10.finance.service;



import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.repo.FindataMetadataApiRepo;
import com.tcarroll10.findata.service.FindataApiServiceImpl;
import com.tcarroll10.findata.service.FindataValidatorServiceImpl;

public class FindataApiServiceImplTest2 {

  private FindataApiServiceImpl service;
  private FindataApiRepo dataRepo;
  private FindataMetadataApiRepo metaRepo;
  private FindataValidatorServiceImpl validator;

  @BeforeEach
  void setUp() {
    dataRepo = mock(FindataApiRepo.class);
    metaRepo = mock(FindataMetadataApiRepo.class);
    validator = mock(FindataValidatorServiceImpl.class);
    service = new FindataApiServiceImpl(dataRepo, metaRepo, validator);
  }

  @Test
  void validateRequestInput_ValidInput_ReturnsResponseEntity() {
    // Arrange
    String dataset = "test";
    Map<String, String> paramsMap = Collections.emptyMap();
    when(validator.validateDataset(dataset)).thenReturn(Optional.empty());
    when(validator.validateParamsKeys(paramsMap)).thenReturn(Optional.empty());
    when(validator.validateFields(dataset, Collections.emptyMap())).thenReturn(Optional.empty());
    when(dataRepo.getData(dataset, Collections.emptyMap())).thenReturn(Collections.emptyList());
    when(metaRepo.getMetaData(dataset)).thenReturn(Collections.emptyMap());

    // Act
    ResponseEntity<?> response = service.validateRequestInput(dataset, paramsMap);

    // Assert
    Assertions.assertNotNull(response);
    verify(dataRepo).getData(dataset, Collections.emptyMap());
    verify(metaRepo).getMetaData(dataset);
  }

  @Test
  void processRequestNoParams_ValidInput_ReturnsResponseEntity() {
    // Arrange
    String dataset = "test";
    when(validator.validateDataset(dataset)).thenReturn(Optional.empty());
    when(dataRepo.getData(dataset)).thenReturn(Collections.emptyList());
    when(metaRepo.getMetaData(dataset)).thenReturn(Collections.emptyMap());

    // Act
    ResponseEntity<?> response = service.processRequestNoParams(dataset);

    // Assert
    Assertions.assertNotNull(response);
    verify(dataRepo).getData(dataset);
    verify(metaRepo).getMetaData(dataset);
  }
}


