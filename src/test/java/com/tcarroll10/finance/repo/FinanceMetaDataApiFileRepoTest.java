package com.tcarroll10.finance.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.tcarroll10.findata.repo.FinMetaDataApiRepo;

@SpringBootTest
public class FinanceMetaDataApiFileRepoTest {

    @Autowired
    FinMetaDataApiRepo repo;

    @Test
    public void getMetaDataTest() {

        Map<String, String> testMap = new HashMap<>();

        testMap.put("fields", "record_date,security_desc");

        Map<String, Map<String, String>> result = repo.getMetaData("Security",
                testMap);

        assertNotNull(result);
        assertTrue(result.containsKey("labels"));
        assertTrue(result.containsKey("dataTypes"));
        assertTrue(result.containsKey("dataFormats"));

        System.out.println(result.toString());

        assertEquals("Record Date", result.get("labels").get("record_date"));
        assertEquals("Security Description",
                result.get("labels").get("security_desc"));

    }

}
