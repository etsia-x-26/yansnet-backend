package com.etsia.interaction.infrastructure.controller;

import com.etsia.common.domain.model.NetworkStatsDto;
import com.etsia.common.domain.model.NetworkSuggestionDto;
import com.etsia.interaction.domain.service.NetworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/network")
@RequiredArgsConstructor
@Tag(name = "Network Management", description = "Endpoints for user network dashboard, connections, and suggestions")
public class NetworkController {

    private final NetworkService networkService;

    @Operation(summary = "Get network stats", description = "Returns counts for connections, contacts, and channels")
    @GetMapping("/stats/{userId}")
    public ResponseEntity<NetworkStatsDto> getStats(@PathVariable Integer userId) {
        return ResponseEntity.ok(networkService.getStats(userId));
    }

    @Operation(summary = "Get network suggestions", description = "Returns a list of suggested people to connect with")
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<List<NetworkSuggestionDto>> getSuggestions(@PathVariable Integer userId) {
        return ResponseEntity.ok(networkService.getSuggestions(userId));
    }
}
