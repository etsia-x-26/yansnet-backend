package com.etsia.interaction.infrastructure.controller;

import com.etsia.common.infrastructure.entities.Channel;
import com.etsia.interaction.domain.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService service;

    public ChannelController(ChannelService service) { this.service = service; }

    @GetMapping
    public List<Channel> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Channel byId(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Channel create(@RequestBody Channel c) { return service.create(c); }

    @PutMapping("/{id}")
    public Channel update(@PathVariable Integer id, @RequestBody Channel c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) { service.delete(id); }
}