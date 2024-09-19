package com.tcarroll10.finance.repo;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.tcarroll10.findata.repo.FindataApiRepoJdbcImpl;

public class FindataApiRepoJdbcImplTest {

  @Mock
  private NamedParameterJdbcTemplate jdbcTemplate;

  @InjectMocks
  private FindataApiRepoJdbcImpl repo;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetData() {
    // Arrange
    String dataset = "test_table";
    String expectedSql = "SELECT * FROM " + dataset;

    // Mock the result
    Map<String, Object> row1 = new HashMap<>();
    row1.put("column1", "value1");
    row1.put("column2", "value2");

    Map<String, Object> row2 = new HashMap<>();
    row2.put("column1", "value3");
    row2.put("column2", "value4");

    List<Map<String, Object>> mockResult = Arrays.asList(row1, row2);

    // Set up mock behavior
    when(jdbcTemplate.queryForList(eq(expectedSql), any(MapSqlParameterSource.class)))
        .thenReturn(mockResult);

    // Act
    List<Map<String, Object>> result = repo.getData(dataset);

    // Assert
    assertEquals(mockResult, result); // Verify result matches mock data

    // Capture and verify SQL query
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    verify(jdbcTemplate).queryForList(sqlCaptor.capture(), any(MapSqlParameterSource.class));

    String actualSql = sqlCaptor.getValue();
    System.out.println("Captured SQL: " + actualSql); // This will help troubleshoot
    assertEquals(expectedSql, actualSql); // Verify the SQL matches

  }

  @Test
  public void testGetDataWithParams() {
    // Arrange
    String dataset = "test_table";

    String expectedSql = """
        SELECT column1, column2
        FROM test_table
        LIMIT 100
        OFFSET 0
        """;



    // Mock the result
    Map<String, Object> row1 = new HashMap<>();
    row1.put("column1", "value1");
    row1.put("column2", "value2");

    Map<String, Object> row2 = new HashMap<>();
    row2.put("column1", "value3");
    row2.put("column2", "value4");

    List<Map<String, Object>> mockResult = Arrays.asList(row1, row2);

    // Set up mock behavior
    when(jdbcTemplate.queryForList(anyString(), any(MapSqlParameterSource.class)))
        .thenReturn(mockResult);

    Map<String, String> sqlMap = new HashMap<>();
    sqlMap.put("fields", "column1, column2");

    // Act
    List<Map<String, Object>> result = repo.getData(dataset, sqlMap);

    // Assert
    assertEquals(mockResult, result); // Verify result matches mock data

    // Capture and verify SQL query
    ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
    verify(jdbcTemplate).queryForList(sqlCaptor.capture(), any(MapSqlParameterSource.class));

    String actualSql = sqlCaptor.getValue();
    System.out.println("Captured SQL: " + actualSql); // This will help troubleshoot
    assertEquals(expectedSql.trim(), actualSql.trim()); // Verify the SQL matches

  }
}
