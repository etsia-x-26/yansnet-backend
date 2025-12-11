package com.etsia.interaction.infrastructure.controller.conversation;

import com.etsia.interaction.application.service.conversation.*;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.FindAllUserByConversationDto;
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
    private final FollowConversationUseCase followConversationUseCase;
    private final FindAllUserByConversation findAllUserByConversation;

    @GetMapping
    public ResponseEntity<List<ConversationDto>> findAll() {
        try{
            List<ConversationDto> conversations = findAllConversationUseCase.execute();
            return ResponseEntity.ok(conversations);
        } catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> findById(@PathVariable("id") Integer id) {
        try{
            ConversationDto dto = findConversationUseCase.execute(id).get();
            return ResponseEntity.ok(dto);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping
    public ResponseEntity<FindAllUserByConversationDto> create(@RequestBody CreateConversationDto request) {
        try{
            // La logique de génération du titre pour les conversations PRIVATE
            // est maintenant gérée dans ConversationRepositoryImpl
            ConversationDto created = createConversationUseCase.execute(request);

            // Si la conversation est privée et qu'il y a des followers à ajouter,
            // utiliser le contrôleur "follow" de FollowConversationController.
            if (created.getType() != null 
                && request.getFollowerRoles() != null 
                && !request.getFollowerRoles().isEmpty()) {

                // Préparer l'appel à FollowConversationController pour ajouter les followers après la création
                // Ici nous injectons le service directement pour garder la couche service commune.
                // NOTE : Modifiez le design si besoin pour déplacer/restructurer cette logique (e.g., un DomainService).

                // Suivre le pattern de "FollowConversationController"
                followConversationUseCase.execute(
                    request.getFollowerRoles(), 
                    created.getId()
                );
            }
            
            // Return the conversation with its users
            FindAllUserByConversationDto result = findAllUserByConversation.executeSingle(created.getId());
            return ResponseEntity.created(URI.create("/Conversation/" + created.getId()))
                    .body(result);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationDto> update(@PathVariable("id") Integer id,
                                                  @RequestBody UpdateConversationDto request) {
        try{
            //request.setId(id);
            ConversationDto updated = updateConversationUseCase.execute(request,id);
            return ResponseEntity.ok(updated);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        try{
            deleteConversationUseCase.execute(id);
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            e.printStackTrace(); // Pour le débogage
            return ResponseEntity.badRequest().build();
        }
    }
}
