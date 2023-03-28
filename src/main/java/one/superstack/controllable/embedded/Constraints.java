package one.superstack.controllable.embedded;

import one.superstack.controllable.enums.DataType;
import one.superstack.controllable.exception.ClientException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Constraints implements Serializable {

    private Object min;

    private Object max;

    private Long minLength;

    private Long maxLength;

    private String pattern;

    public Constraints() {

    }

    public Constraints(Object min, Object max, Long minLength, Long maxLength, String pattern) {
        this.min = min;
        this.max = max;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
    }

    public Object getMin() {
        return min;
    }

    public void setMin(Object min) {
        this.min = min;
    }

    public Object getMax() {
        return max;
    }

    public void setMax(Object max) {
        this.max = max;
    }

    public Long getMinLength() {
        return minLength;
    }

    public void setMinLength(Long minLength) {
        this.minLength = minLength;
    }

    public Long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void validate(Object data, DataType dataType) {
        if (null == data) {
            throw new ClientException("Data is missing");
        }

        switch (dataType) {
            case OBJECT -> validateObject((LinkedHashMap<?, ?>) data);
            case ARRAY -> validateArray((ArrayList<?>) data);
            case STRING -> validateString((String) data);
            case INTEGER -> validateInteger((Integer) data);
            case FLOAT -> validateFloat((Double) data);
        }
    }

    private void validateInteger(Integer data) {
        if (null != this.max) {
            if (data > (Integer) this.max) {
                throw new ClientException("Value is greater than the maximum allowed value");
            }
        }

        if (null != this.min) {
            if (data < (Integer) min) {
                throw new ClientException("Value is smaller than the minimum allowed value");
            }
        }
    }

    private void validateFloat(Double data) {
        if (null != this.max) {
            if (data > (Double) this.max) {
                throw new ClientException("Value is greater than the maximum allowed value");
            }
        }

        if (null != this.min) {
            if (data < (Double) this.min) {
                throw new ClientException("Value is smaller than the minimum allowed value");
            }
        }
    }

    private void validateString(String data) {
        if (null != this.pattern && !this.pattern.isBlank()) {
            if (data.matches(this.pattern)) {
                throw new ClientException("Value does not match the allowed pattern");
            }
        }

        if (null != this.minLength) {
            if (data.length() < this.minLength) {
                throw new ClientException("Value is shorter than the minimum allowed length");
            }
        }

        if (null != this.maxLength) {
            if (data.length() > this.maxLength) {
                throw new ClientException("Value is longer than the maximum allowed length");
            }
        }
    }

    private void validateObject(LinkedHashMap<?, ?> data) {
        if (null != this.minLength) {
            if (data.size() < this.minLength) {
                throw new ClientException("Value is shorter than the minimum allowed length");
            }
        }

        if (null != this.maxLength) {
            if (data.size() > this.maxLength) {
                throw new ClientException("Value is longer than the maximum allowed length");
            }
        }
    }

    private void validateArray(ArrayList<?> data) {
        if (null != this.minLength) {
            if (data.size() < this.minLength) {
                throw new ClientException("Value is shorter than the minimum allowed length");
            }
        }

        if (null != this.maxLength) {
            if (data.size() > this.maxLength) {
                throw new ClientException("Value is longer than the maximum allowed length");
            }
        }
    }
}
