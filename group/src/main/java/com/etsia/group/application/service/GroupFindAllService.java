package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupFindAllService {

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public List<GroupDto> execute() {
        return groupDomainService.findAll();
    }
}
