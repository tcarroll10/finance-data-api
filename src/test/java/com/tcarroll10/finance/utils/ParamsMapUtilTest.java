package com.tcarroll10.finance.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParamsMapUtilTest {


  private Map<String, String> testMap;

  @BeforeEach
  void setup() {
    testMap = new HashMap<>();

  }

  @Test
  void processFieldsTest() {

    // fields param not present
    assertEquals("", ParamsMapUtil.processFields(testMap));

    // null
    testMap.put("fields", null);
    assertEquals("", ParamsMapUtil.processFields(testMap));

    // empty string
    testMap.put("fields", "");
    assertEquals("", ParamsMapUtil.processFields(testMap));

    // single field
    testMap.put("fields", "f1");
    assertEquals("f1", ParamsMapUtil.processFields(testMap));

    // multiple fields
    testMap.put("fields", "f1, f2, f3");
    assertEquals("f1, f2, f3", ParamsMapUtil.processFields(testMap));

    // multiple fields with whitespace
    // multiple fields
    testMap.put("fields", "f1,  f2,f3");
    assertEquals("f1, f2, f3", ParamsMapUtil.processFields(testMap));

  }


  @Test
  void processFiltersTest() {

    // filter param not present
    assertEquals("", ParamsMapUtil.processFilters(testMap));

    // null
    testMap.put("filter", null);
    assertEquals("", ParamsMapUtil.processFilters(testMap));

    // empty
    testMap.put("filter", "");
    assertEquals("", ParamsMapUtil.processFilters(testMap));

    // invalid filter
    testMap.put("filter", "record_date:eq:15");
    assertEquals("record_date = '15'", ParamsMapUtil.processFilters(testMap));

    // one filter
    testMap.put("filter", "record_date:eq:2023-11-30");
    assertEquals("record_date = '2023-11-30'", ParamsMapUtil.processFilters(testMap));

    // one filter
    testMap.put("filter",
        "record_date:eq:2023-11-30,record_date:gte:2023-11-30,record_date:lt:2023-11-30");
    assertEquals(
        "record_date = '2023-11-30' and record_date >= '2023-11-30' and record_date < '2023-11-30'",
        ParamsMapUtil.processFilters(testMap));

    // in operator one input
    testMap.put("filter", "record_date:in:(2023-11-30)");
    assertEquals("record_date in ('2023-11-30')", ParamsMapUtil.processFilters(testMap));

    // in operator multiple inputs
    testMap.put("filter", "record_date:in:(2023-09-30,2023-10-31)");
    assertEquals("record_date in ('2023-09-30','2023-10-31')",
        ParamsMapUtil.processFilters(testMap));

    // in operator multiple inputs
    testMap.put("filter",
        "record_date:in:(2023-09-30,2023-10-31),record_date:in:(2023-09-30,2023-10-31) ");
    assertEquals(
        "record_date in ('2023-09-30','2023-10-31') and record_date in ('2023-09-30','2023-10-31')",
        ParamsMapUtil.processFilters(testMap));

  }


}
