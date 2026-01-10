//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.etsia.channel;

import com.etsia.common.infrastructure.entities.Channel;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    private final JpaChannelRepository jpaChannelRepository;

    public ChannelService(JpaChannelRepository jpaChannelRepository) {
        this.jpaChannelRepository = jpaChannelRepository;
    }

    public Channel createGroup(String title, String description) {
        Channel group = new Channel(title, description);
        return jpaChannelRepository.save(group);
    }

    public Optional<Channel> getGroupById(Long id) {
        return jpaChannelRepository.findById(id);
    }
}
