package com.etsia.common.domain.model.sub;

import lombok.Getter;

@Getter
public enum ConversationType {
    PRIVATE("private"),
    PUBLIC("group");
    private final String value;

    ConversationType(String value) {
        this.value = value;
    }
}
