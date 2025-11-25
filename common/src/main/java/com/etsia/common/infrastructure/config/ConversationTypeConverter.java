package com.etsia.common.infrastructure.config;

import com.etsia.common.domain.model.sub.ConversationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ConversationTypeConverter implements AttributeConverter<ConversationType, String> {

    @Override
    public String convertToDatabaseColumn(ConversationType conversationType) {
        return conversationType != null ? conversationType.getValue() : null;
    }

    @Override
    public ConversationType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        for (ConversationType type : ConversationType.values()) {
            if (type.getValue().equalsIgnoreCase(dbData)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Valeur inconnue pour ConversationType: " + dbData);
    }
}

