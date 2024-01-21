package com.tcarroll10.finance.repo;

import java.util.Map;

/**
 * Repo interface for Finance data api service.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

public interface FinanceMetaDataApiRepo {

    public Map<String, Map<String, String>> getMetaData(String dataset,
            Map<String, String> paramsMap);

    public Map<String, Map<String, String>> getMetaData(String dataset);

}
