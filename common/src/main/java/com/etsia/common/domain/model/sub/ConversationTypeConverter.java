package com.etsia.common.domain.model.sub;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter
public class ConversationTypeConverter implements AttributeConverter<ConversationType, String> {
    
    @Override
    public String convertToDatabaseColumn(ConversationType type) {
        if (type == null) {
            return null;
        }
        return type.getValue();
    }

    @Override
    public ConversationType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        return Arrays.stream(ConversationType.values())
            .filter(type -> type.getValue().equals(dbData))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Unknown ConversationType value: " + dbData
            ));
    }
}
