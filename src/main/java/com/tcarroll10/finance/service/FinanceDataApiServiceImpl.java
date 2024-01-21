package com.tcarroll10.finance.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcarroll10.finance.domain.ErrorMsg;
import com.tcarroll10.finance.domain.Meta;
import com.tcarroll10.finance.domain.OutDataTo;
import com.tcarroll10.finance.repo.FinanceDataApiRepo;
import com.tcarroll10.finance.repo.FinanceMetaDataApiRepo;

/**
 * Service for Finance data API service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

@Service
public class FinanceDataApiServiceImpl implements FinanceDataApiService {

    private final FinanceDataApiRepo dataRepo;

    private final FinanceMetaDataApiRepo metaRepo;

    private final static String[] validKeys = {"fields", "sort", "filter",
            "format", "page", "per_page"};

    /**
     * Constructor for controller allows service injection.
     * 
     * @param repo interface used for querying the datasource.
     * 
     */

    public FinanceDataApiServiceImpl(FinanceDataApiRepo dataRepo,
            FinanceMetaDataApiRepo metaRepo) {

        this.dataRepo = dataRepo;
        this.metaRepo = metaRepo;
    }

    @Override
    public ResponseEntity<?> validateRequestInput(String dataset,
            Map<String, String> paramsMap) {

        String badKey = "";

        boolean isValid = true;
        for (String key : paramsMap.keySet()) {
            if (!Arrays.asList(validKeys).contains(key)) {
                badKey = key;
                isValid = false;

                break;

            }

        }

        if (!isValid)

        {
            // If validation fails, return an error response with HTTP status
            // 400
            String msg = String.format(
                    "Invalid query parameter '%s' with value '%s'. "
                            + "For more information, please see the documentation.",
                    badKey, paramsMap.get(badKey));
            ErrorMsg error = new ErrorMsg(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // If validation passes, return a successful response with HTTP status
        List<Map<String, Object>> data = dataRepo.getData(dataset, paramsMap);
        Map<String, Map<String, String>> metaData = metaRepo
                .getMetaData(dataset, paramsMap);
        Meta meta = Meta.builder().totalCount(data.size())
                .dataFormats(metaData.get("dataFormats"))
                .labels(metaData.get("labels"))
                .dataTypes(metaData.get("dataTypes")).build();

        OutDataTo output = OutDataTo.builder().data(data).meta(meta).build();

        return ResponseEntity.ok().body(output);

    }

    @Override
    public ResponseEntity<?> processRequestNoParams(String dataset) {

        List<Map<String, Object>> data = dataRepo.getData(dataset);
        Map<String, Map<String, String>> metaData = metaRepo
                .getMetaData(dataset);
        Meta meta = Meta.builder().totalCount(data.size())
                .dataFormats(metaData.get("dataFormats"))
                .labels(metaData.get("labels"))
                .dataTypes(metaData.get("dataTypes")).build();

        OutDataTo output = OutDataTo.builder().data(data).meta(meta).build();

        return ResponseEntity.ok().body(output);
    }
}
