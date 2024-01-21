package com.tcarroll10.finance.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SamplDataObject {

    @JsonProperty("record_date")
    private Date recordDate;

    @JsonProperty("security_type_desc")
    private String securityTypeDesc;

    @JsonProperty("security_desc")
    private String securityDesc;

    @JsonProperty("avg_interest_rate_amt")
    private BigDecimal avgInterestRateAmt;

    public SamplDataObject(Date recordDate, String securityTypeDesc,
            String securityDesc, BigDecimal avgInterestRateAmt) {

        this.recordDate = recordDate;
        this.securityTypeDesc = securityTypeDesc;
        this.securityDesc = securityDesc;
        this.avgInterestRateAmt = avgInterestRateAmt;
    }

    public SamplDataObject() {

    }

}
