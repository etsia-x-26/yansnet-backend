package com.etsia.common.infrastructure.config;


import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ConversationRoleConverter implements AttributeConverter<ConversationRole, String> {

    @Override
    public String convertToDatabaseColumn(ConversationRole conversationRole) {
        return conversationRole != null ? conversationRole.getValue() : null;
    }

    @Override
    public ConversationRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        for (ConversationRole role : ConversationRole.values()) {
            if (role.getValue().equalsIgnoreCase(dbData)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Valeur inconnue pour ConversationRole: " + dbData);
    }
}
