package com.etsia.common.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/docs/websocket")
@RequiredArgsConstructor
@Tag(name = "Documentation", description = "Endpoints for serving documentation")
public class WebSocketDocsController {

    @Operation(summary = "Get WebSocket Guide", description = "Returns the WebSocket integration guide in Markdown format")
    @GetMapping(produces = MediaType.TEXT_MARKDOWN_VALUE)
    public ResponseEntity<String> getWebSocketGuide() {
        try {
            Resource resource = new ClassPathResource("websocket_guide.md");
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                String content = FileCopyUtils.copyToString(reader);
                return ResponseEntity.ok(content);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
