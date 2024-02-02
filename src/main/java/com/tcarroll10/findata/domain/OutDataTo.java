package com.tcarroll10.findata.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = OutDataTo.Builder.class)
public class OutDataTo {

    List<Map<String, Object>> data;

    Meta meta;

    private OutDataTo(Builder builder) {
        this.data = builder.data;
        this.meta = builder.meta;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(OutDataTo outDataTo) {
        return new Builder(outDataTo);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private List<Map<String, Object>> data = Collections.emptyList();
        private Meta meta;

        private Builder() {
        }

        private Builder(OutDataTo outDataTo) {
            this.data = outDataTo.data;
            this.meta = outDataTo.meta;
        }

        public Builder data(List<Map<String, Object>> data) {
            this.data = data;
            return this;
        }

        public Builder meta(Meta meta) {
            this.meta = meta;
            return this;
        }

        public OutDataTo build() {
            return new OutDataTo(this);
        }
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

}
