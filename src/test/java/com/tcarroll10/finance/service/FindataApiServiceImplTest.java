package com.tcarroll10.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.tcarroll10.findata.domain.OutDataTo;
import com.tcarroll10.findata.repo.FindataApiRepo;
import com.tcarroll10.findata.repo.FindataMetadataApiRepo;
import com.tcarroll10.findata.service.FindataApiServiceImpl;
import com.tcarroll10.findata.service.FindataValidatorServiceImpl;


public class FindataApiServiceImplTest {

  @Mock
  private FindataApiRepo dataRepo;

  @Mock
  private FindataMetadataApiRepo metaRepo;

  @Mock
  private FindataValidatorServiceImpl validator;

  @InjectMocks
  private FindataApiServiceImpl service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetDataReturnsExpectedList() {
    // Given a dataset name and mock params
    String dataset = "test_dataset";
    Map<String, String> paramsMap = new HashMap<>();
    paramsMap.put("format", "json");

    // Mock the validator to bypass validation logic (assume valid input)
    when(validator.validateDataset(dataset)).thenReturn(Optional.empty());
    when(validator.validateParamsKeys(paramsMap)).thenReturn(Optional.empty());
    when(validator.validateFields(eq(dataset), any())).thenReturn(Optional.empty());
    when(validator.validateFormat(eq(dataset), any())).thenReturn(Optional.empty());

    // Create the specific mock data to return from dataRepo
    List<Map<String, Object>> mockData =
        List.of(Map.of("field1", "value1"), Map.of("field2", "value2"));
    when(dataRepo.getData(eq(dataset), any())).thenReturn(mockData);

    // Mock metaRepo to return metadata (even though it's not the focus of this test)
    Map<String, Map<String, String>> mockMeta = Map.of("labels",
        Map.of("field1", "Label 1", "field2", "Label 2"), "dataFormats", Map.of("format1", "json"));
    when(metaRepo.getMetaData(dataset)).thenReturn(mockMeta);

    // When the service is called
    ResponseEntity<?> response = service.validateRequestInput(dataset, paramsMap);

    // Then
    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() instanceof OutDataTo); // Check that the body is of type OutDataTo

    // Extract the data from the response
    OutDataTo output = (OutDataTo) response.getBody();
    assertNotNull(output);
    assertEquals(mockData, output.getData()); // Ensure the service returns the mocked data
  }

}
