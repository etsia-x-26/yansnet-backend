//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.etsia.channel;
import com.etsia.common.infrastructure.entities.Channel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"channel"})
@CrossOrigin("*")
class ChannelController {

    private final ChannelService channelService;

    ChannelController( ChannelService channelService) {
        this.channelService = channelService;
    }

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

