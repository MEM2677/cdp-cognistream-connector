package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Community;
import it.keybiz.cdp.innova.domain.Community_;
import it.keybiz.cdp.innova.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;

    @Override
    public List<Community> findAll() {
        return communityRepository.findAll(Sort.by(Community_.NAME));
    }
}
