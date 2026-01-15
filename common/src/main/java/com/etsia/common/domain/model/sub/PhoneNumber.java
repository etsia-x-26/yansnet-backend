package com.etsia.common.domain.model.sub;

import java.io.Serializable;

public record PhoneNumber(String value) implements Serializable {
    public PhoneNumber {
        if (value != null && !value.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
