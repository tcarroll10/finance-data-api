package com.tcarroll10.findata.domain;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * Error message POJO class.
 * 
 * @author tom carroll
 * @version 2023-12-27
 */

@JsonDeserialize(builder = ErrorMsg.Builder.class)
public class ErrorMsg {

    private String error;
    private String message;

    private ErrorMsg(Builder builder) {
        this.error = builder.error;
        this.message = builder.message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ErrorMsg errorMsg) {
        return new Builder(errorMsg);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private String error;
        private String message;

        private Builder() {
        }

        private Builder(ErrorMsg errorMsg) {
            this.error = errorMsg.error;
            this.message = errorMsg.message;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorMsg build() {
            return new ErrorMsg(this);
        }
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, message);
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
        return Objects.equals(error, other.error)
                && Objects.equals(message, other.message);
    }

}
