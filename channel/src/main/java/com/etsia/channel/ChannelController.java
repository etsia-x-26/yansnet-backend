//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.etsia.channel;
import com.etsia.common.infrastructure.entities.Channel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"channel"})
class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        try {
            System.out.println(request.getName());
            Channel group = this.channelService.createGroup(request.getName(), request.getDescription());
            return ResponseEntity.status(201).body(group);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        Optional<Channel> group = this.channelService.getGroupById(id);
        return group.isPresent() ? ResponseEntity.ok((Channel)group.get()) : ResponseEntity.notFound().build();
    }

}

