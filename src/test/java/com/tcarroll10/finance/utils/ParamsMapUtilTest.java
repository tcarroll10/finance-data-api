package com.tcarroll10.finance.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tcarroll10.findata.utils.ParamsMapHelper;
import com.tcarroll10.findata.utils.ParamsMapUtil;

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


  void processSortTest() {

    // sort param not present
    assertEquals("", ParamsMapHelper.processSort(testMap));

    // null
    testMap.put("sort", null);
    assertEquals("", ParamsMapHelper.processSort(testMap));

    // empty
    testMap.put("sort", "");
    assertEquals("", ParamsMapHelper.processSort(testMap));

    // one element sort DESC
    testMap.put("sort", "-record_date");
    assertEquals("record_date DESC", ParamsMapHelper.processSort(testMap));

    // one element sort DESC
    testMap.put("sort", "record_date");
    assertEquals("record_date ASC", ParamsMapHelper.processSort(testMap));

    // two element sort DESC, DESC
    testMap.put("sort", "-f1,-f2");
    assertEquals("f1 DESC, f2 DESC", ParamsMapHelper.processSort(testMap));

    // two element sort DESC, ASC
    testMap.put("sort", "-f1,f2");
    assertEquals("f1 DESC, f2 ASC", ParamsMapHelper.processSort(testMap));

    // two element sort ASC,ASC
    testMap.put("sort", "f1,f2");
    assertEquals("f1 ASC, f2 ASC", ParamsMapHelper.processSort(testMap));

    // two element sort ASC,DESC
    testMap.put("sort", "f1,-f2");
    assertEquals("f1 ASC, f2 DESC", ParamsMapHelper.processSort(testMap));

  }


}
