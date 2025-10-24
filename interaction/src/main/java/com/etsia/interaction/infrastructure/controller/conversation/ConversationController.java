package com.etsia.interaction.infrastructure.controller.conversation;

import com.etsia.interaction.application.service.conversation.FindAllConversationUseCase;
import com.etsia.interaction.application.service.conversation.FindConversationUseCase;
import com.etsia.interaction.application.service.conversation.UpdateConversationUseCase;
import com.etsia.interaction.application.service.conversation.CreateConversationUseCase;
import com.etsia.interaction.application.service.conversation.DeleteConversationUseCase;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;

import com.etsia.common.domain.model.ConversationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/Conversation")
@CrossOrigin("*")
@AllArgsConstructor
public class ConversationController {

    private final CreateConversationUseCase createConversationUseCase;
    private final DeleteConversationUseCase deleteConversationUseCase;
    private final FindConversationUseCase findConversationUseCase;
    private final UpdateConversationUseCase updateConversationUseCase;
    private final FindAllConversationUseCase findAllConversationUseCase;

    @GetMapping
    public ResponseEntity<List<ConversationDto>> findAll() {
        try{
            List<ConversationDto> conversations = findAllConversationUseCase.execute();
            return ResponseEntity.ok(conversations);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> findById(@PathVariable("id") Integer id) {
        try{
            ConversationDto dto = findConversationUseCase.execute(id);
            return ResponseEntity.ok(dto);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<ConversationDto> create(@RequestBody CreateConversationDto request) {
        try{
            ConversationDto created = createConversationUseCase.execute(request);
            return ResponseEntity.created(URI.create("/Conversation/" + created.getId()))
                    .body(created);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationDto> update(@PathVariable("id") Integer id,
                                                  @RequestBody UpdateConversationDto request) {
        try{
            request.setId(id);
            ConversationDto updated = updateConversationUseCase.execute(request);
            return ResponseEntity.ok(updated);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        try{
            deleteConversationUseCase.execute(id);
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
