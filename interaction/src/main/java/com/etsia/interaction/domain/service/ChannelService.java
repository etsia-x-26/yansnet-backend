package com.etsia.interaction.domain.service;

import com.etsia.common.infrastructure.entities.Channel;
import com.etsia.interaction.infrastructure.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private final ChannelRepository repo;

    public ChannelService(ChannelRepository repo) { this.repo = repo; }

    public List<Channel> findAll()               { return repo.findAll(); }
    public Channel findById(Integer id)          { return repo.findById(id).orElseThrow(); }
    public Channel create(Channel c)             { return repo.save(c); }
    public Channel update(Integer id, Channel c) { c.setId(id); return repo.save(c); }
    public void delete(Integer id)               { repo.deleteById(id); }
}