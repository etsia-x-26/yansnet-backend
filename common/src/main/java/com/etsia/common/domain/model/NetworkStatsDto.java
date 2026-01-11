package com.etsia.common.domain.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class NetworkStatsDto implements Serializable {
    long connectionsCount;
    long contactsCount;
    long channelsCount;
}
