package com.etsia.interaction.domain.service;

import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.interaction.infrastructure.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    private final ConversationRepository repo;

    public ConversationService(ConversationRepository repo) { this.repo = repo; }

    public List<Conversation> findAll()                    { return repo.findAll(); }
    public Conversation findById(Integer id)               { return repo.findById(id).orElseThrow(); }
    public Conversation create(Conversation c)             { return repo.save(c); }
    public Conversation update(Integer id, Conversation c) { c.setId(id); return repo.save(c); }
    public void delete(Integer id)                         { repo.deleteById(id); }
}