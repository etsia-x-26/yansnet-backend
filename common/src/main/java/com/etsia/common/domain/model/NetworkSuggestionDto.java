package com.etsia.common.domain.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class NetworkSuggestionDto implements Serializable {
    UserDto user;
    long mutualConnectionsCount;
    String reason;
}
