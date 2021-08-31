package com.playground.argument;

import java.util.List;
import java.util.Objects;

public class Argument {
    private final String name;
    private final String description;
    private final List<String> keywords;
    private final int valueCount;

    private Argument(
           String name,
           String description,
           List<String> keywords,
           int valueCount) {
        this.name = name;
        this.description = description;
        this.keywords = keywords;
        this.valueCount = valueCount;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    List<String> getKeywords() {
        return keywords;
    }

    int getValueCount() {
        return valueCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Argument argument = (Argument) o;
        return name.equals(argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {
        private String name;
        private String description;
        private List<String> keywords;
        private int valueCount;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setKeywords(List<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public Builder setValueCount(int valueCount) {
            this.valueCount = valueCount;
            return this;
        }

        public Argument build() {
            return new Argument(name, description, keywords, valueCount);
        }
    }
}
