package com.etsia.interaction.infrastructure.controller;

import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.interaction.domain.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService service;

    public ConversationController(ConversationService service) { this.service = service; }

    @GetMapping
    public List<Conversation> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Conversation byId(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Conversation create(@RequestBody Conversation c) { return service.create(c); }

    @PutMapping("/{id}")
    public Conversation update(@PathVariable Integer id, @RequestBody Conversation c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) { service.delete(id); }
}