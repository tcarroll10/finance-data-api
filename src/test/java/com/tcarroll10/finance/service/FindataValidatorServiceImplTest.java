package com.tcarroll10.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.tcarroll10.findata.domain.ErrorMsg;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.repo.FindataMetadataApiRepo;
import com.tcarroll10.findata.service.FindataValidatorServiceImpl;
import com.tcarroll10.findata.utils.Const;

public class FindataValidatorServiceImplTest {


  @Mock
  private FindataApiRepo repo;

  @Mock
  private FindataMetadataApiRepo metaRepo;

  @InjectMocks
  private FindataValidatorServiceImpl service;

  @Mock
  Map<String, Map<String, String>> metaData;



  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidDataset() {
    // Arrange: Mock metaRepo to return non-empty metadata for a valid dataset
    String validDataset = "Security";
    Map<String, Map<String, String>> mockMetaData = new HashMap<>();
    mockMetaData.put("key1", new HashMap<>()); // Simulate some metadata
    when(metaRepo.getMetaData(validDataset)).thenReturn(mockMetaData);

    // Act: Call the method with a valid dataset
    Optional<ResponseEntity<?>> result = service.validateDataset(validDataset);

    // Assert: The result should be an empty Optional
    assertEquals(Optional.empty(), result);
  }

  @Test
  public void testBadDataSet() {

    Optional<?> result = service.validateDataset("badDataset");

    String msg = String.format("Path parameter localhost:8080/api/v2/%s "
        + "does not exist, for more information please see the documentation.", "badDataset");
    ErrorMsg error = ErrorMsg.builder().error(Const.BAD_DATASET).message(msg).build();

    assertEquals(Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)), result);

  }

  @Test
  public void testGoodKeys() {

    final Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("fields", "field1,field2");
    paramsMap.put("filter", "filter1");
    paramsMap.put("sort", "asc");
    paramsMap.put("format", "json");
    paramsMap.put("page", "1");
    paramsMap.put("per_page", "10");


    Optional<?> result = service.validateParamsKeys(paramsMap);


    assertEquals(Optional.empty(), result);

  }

  @Test
  public void testBadKeys() {

    final Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("bad_fields", "field1");
    paramsMap.put("filter", "filter1");
    paramsMap.put("sort", "asc");
    paramsMap.put("format", "json");
    paramsMap.put("page", "1");
    paramsMap.put("per_page", "10");



    String msg = String.format("Invalid query parameter '%s' with value '%s'. "
        + "For more information, please see the documentation.", "bad_fields", "field1");

    ErrorMsg error = ErrorMsg.builder().error(Const.INVALD_QUERY_PARAMETER).message(msg).build();


    ResponseEntity<ErrorMsg> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);


    Optional<ResponseEntity<?>> result = service.validateParamsKeys(paramsMap);


    assertEquals(Optional.of(expectedResponse), result);

  }



}
