package com.tcarroll10.finance.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParamsMapHelperTest {

    private Map<String, String> testMap;

    @BeforeEach
    void setup() {
        testMap = new HashMap<>();

    }

    @Test
    void processFieldsTest() {

        // fields param not present
        assertEquals("SELECT *", ParamsMapHelper.processFields(testMap));

        // null
        testMap.put("fields", null);
        assertEquals("SELECT *", ParamsMapHelper.processFields(testMap));

        // empty string
        testMap.put("fields", "");
        assertEquals("SELECT *", ParamsMapHelper.processFields(testMap));

        // single field
        testMap.put("fields", "f1");
        assertEquals("SELECT f1", ParamsMapHelper.processFields(testMap));

        // multiple fields
        testMap.put("fields", "f1, f2, f3");
        assertEquals("SELECT f1, f2, f3",
                ParamsMapHelper.processFields(testMap));

        // multiple fiels with whitespace
        // multiple fields
        testMap.put("fields", "f1,  f2,f3");
        assertEquals("SELECT f1, f2, f3",
                ParamsMapHelper.processFields(testMap));

    }

    @Test
    void processFiltersTest() {

        // filter param not present
        assertEquals("", ParamsMapHelper.processFilters(testMap));

        // null
        testMap.put("filter", null);
        assertEquals("", ParamsMapHelper.processFilters(testMap));

        // empty
        testMap.put("filter", "");
        assertEquals("", ParamsMapHelper.processFilters(testMap));

        // invalid filter
        testMap.put("filter", "record_date:eq:15");
        assertEquals("WHERE record_date = '15'",
                ParamsMapHelper.processFilters(testMap));

        // one filter
        testMap.put("filter", "record_date:eq:2023-11-30");
        assertEquals("WHERE record_date = '2023-11-30'",
                ParamsMapHelper.processFilters(testMap));

        // one filter
        testMap.put("filter",
                "record_date:eq:2023-11-30,record_date:gte:2023-11-30,record_date:lt:2023-11-30");
        assertEquals(
                "WHERE record_date = '2023-11-30' and record_date >= '2023-11-30' and record_date < '2023-11-30'",
                ParamsMapHelper.processFilters(testMap));

        // in operator one input
        testMap.put("filter", "record_date:in:(2023-11-30)");
        assertEquals("WHERE record_date in ('2023-11-30')",
                ParamsMapHelper.processFilters(testMap));

        // in operator multiple inputs
        testMap.put("filter", "record_date:in:(2023-09-30,2023-10-31)");
        assertEquals("WHERE record_date in ('2023-09-30','2023-10-31')",
                ParamsMapHelper.processFilters(testMap));

    }

    @Test
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
        assertEquals("ORDER BY record_date DESC",
                ParamsMapHelper.processSort(testMap));

        // one element sort DESC
        testMap.put("sort", "record_date");
        assertEquals("ORDER BY record_date ASC",
                ParamsMapHelper.processSort(testMap));

        // two element sort DESC, DESC
        testMap.put("sort", "-f1,-f2");
        assertEquals("ORDER BY f1 DESC, f2 DESC",
                ParamsMapHelper.processSort(testMap));

        // two element sort DESC, ASC
        testMap.put("sort", "-f1,f2");
        assertEquals("ORDER BY f1 DESC, f2 ASC",
                ParamsMapHelper.processSort(testMap));

        // two element sort ASC,ASC
        testMap.put("sort", "f1,f2");
        assertEquals("ORDER BY f1 ASC, f2 ASC",
                ParamsMapHelper.processSort(testMap));

        // two element sort ASC,DESC
        testMap.put("sort", "f1,-f2");
        assertEquals("ORDER BY f1 ASC, f2 DESC",
                ParamsMapHelper.processSort(testMap));

    }

    @Test
    void processParamsToSqlTest() {

        // empty fields
        testMap.put("fields", "");
        assertEquals("SELECT *" + "\nFROM Security",
                ParamsMapHelper.processParamsMapToSql("Security", testMap));

        // one field
        testMap.put("fields", "record_date");
        assertEquals("SELECT record_date" + "\nFROM Security",
                ParamsMapHelper.processParamsMapToSql("Security", testMap));

        // multiple fields
        testMap.put("fields", "record_date, f1, f2");
        assertEquals("SELECT record_date, f1, f2" + "\nFROM Security",
                ParamsMapHelper.processParamsMapToSql("Security", testMap));

    }

    @Test
    void splitFilterParamTest() {
        String input = "country_currency_desc:in:(Canada-Dollar,Mexico-Peso),record_date:gte:2020-01-01";

        String[] expected = {
                "country_currency_desc:in:(Canada-Dollar,Mexico-Peso)",
                "record_date:gte:2020-01-01"};
        assertArrayEquals(expected, ParamsMapHelper.splitFilterParam(input));

        input = "record_date:gte:2020-01-01, country_currency_desc:in:(Canada-Dollar,Mexico-Peso)";

        String[] expected1 = {"record_date:gte:2020-01-01",
                "country_currency_desc:in:(Canada-Dollar,Mexico-Peso)"};
        assertArrayEquals(expected1, ParamsMapHelper.splitFilterParam(input));

    }

}
