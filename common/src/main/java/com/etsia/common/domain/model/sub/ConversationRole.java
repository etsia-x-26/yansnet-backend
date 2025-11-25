package com.etsia.common.domain.model.sub;

import lombok.Getter;

@Getter
public enum ConversationRole {
    USER("user"),
    ADMIN("admin");
    private final String value;

    ConversationRole(String value) {
        this.value = value;
    }

    //USER,
    //ADMIN
}
