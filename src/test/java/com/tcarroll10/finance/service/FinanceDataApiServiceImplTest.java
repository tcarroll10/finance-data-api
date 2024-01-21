package com.tcarroll10.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tcarroll10.finance.domain.ErrorMsg;
import com.tcarroll10.finance.service.FinanceDataApiService;

@SpringBootTest
public class FinanceDataApiServiceImplTest {

    @Autowired
    private FinanceDataApiService service;

    @Test
    public void validateRequestInput() {

        String badKey = "test";
        Map testMap = new HashMap<>();
        testMap.put(badKey, null);

        // Call the method being tested
        ResponseEntity<?> response = service.validateRequestInput("dummy",
                testMap);

        String msg = String.format(
                "Invalid query parameter '%s' with value '%s'. For more information, please see the documentation.",
                badKey, testMap.get(badKey));

        ErrorMsg err = new ErrorMsg(msg);

        // Validate the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(err, response.getBody());

    }

}
