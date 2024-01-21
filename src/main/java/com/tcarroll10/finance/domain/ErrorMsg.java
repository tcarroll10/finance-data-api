package com.tcarroll10.finance.domain;

import java.util.Objects;

/**
 * Error message POJO class.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

public class ErrorMsg {

    private static final String ERROR = "Invalid Query Param";
    private String message;

    /**
     * Constructor class. Only a single constructor b/c only one variable
     * changes
     * 
     * @param message is the error message returned to client. It will inform
     * user of invalid input.
     */

    public ErrorMsg(String message) {

        this.message = message;
    }

    public String getError() {
        return ERROR;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorMsg other = (ErrorMsg) obj;
        return Objects.equals(message, other.message);
    }

}
