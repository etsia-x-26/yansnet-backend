//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.etsia.channel;

import com.etsia.common.infrastructure.entities.Channel;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ChannelService {
    private final JpaChannelRepository jpaChannelRepository;

    public ChannelService(JpaChannelRepository jpaChannelRepository) {
        this.jpaChannelRepository = jpaChannelRepository;
    }

    @Transactional
    public Channel createGroup(String title, String description) {
        Channel group = new Channel(title, description);
        return (Channel)this.jpaChannelRepository.save(group);
    }

    public Optional<Channel> getGroupById(Long id) {
        return this.jpaChannelRepository.findById(id);
    }
}
