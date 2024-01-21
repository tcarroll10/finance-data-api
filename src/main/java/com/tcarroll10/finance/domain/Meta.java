package com.tcarroll10.finance.domain;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Meta.Builder.class)
public class Meta {

    int pageCount;
    Map<String, String> labels;
    Map<String, String> dataTypes;
    Map<String, String> dataFormats;
    int totalCount;
    int totalPages;

    private Meta(Builder builder) {
        this.pageCount = builder.pageCount;
        this.labels = builder.labels;
        this.dataTypes = builder.dataTypes;
        this.dataFormats = builder.dataFormats;
        this.totalCount = builder.totalCount;
        this.totalPages = builder.totalPages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Meta meta) {
        return new Builder(meta);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private int pageCount;
        private Map<String, String> labels = Collections.emptyMap();
        private Map<String, String> dataTypes = Collections.emptyMap();
        private Map<String, String> dataFormats = Collections.emptyMap();
        private int totalCount;
        private int totalPages;

        private Builder() {
        }

        private Builder(Meta meta) {
            this.pageCount = meta.pageCount;
            this.labels = meta.labels;
            this.dataTypes = meta.dataTypes;
            this.dataFormats = meta.dataFormats;
            this.totalCount = meta.totalCount;
            this.totalPages = meta.totalPages;
        }

        public Builder pageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder labels(Map<String, String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder dataTypes(Map<String, String> dataTypes) {
            this.dataTypes = dataTypes;
            return this;
        }

        public Builder dataFormats(Map<String, String> dataFormats) {
            this.dataFormats = dataFormats;
            return this;
        }

        public Builder totalCount(int totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public Builder totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Meta build() {
            return new Meta(this);
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public Map<String, String> getDataTypes() {
        return dataTypes;
    }

    public Map<String, String> getDataFormats() {
        return dataFormats;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
