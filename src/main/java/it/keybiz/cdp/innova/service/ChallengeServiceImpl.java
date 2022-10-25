package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.dto.ChallengeSearchDTO;
import it.keybiz.cdp.innova.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Override
    public Page<Challenge> findAllPaged(ChallengeSearchDTO searchDTO, Pageable pageable) {
        log.info("Requested Challenge paged list: filters: {}", searchDTO);
        return challengeRepository.findAll(ChallengeRepository.Specifications.filter(searchDTO), pageable);
    }

    @Override
    public List<Challenge> findAll(ChallengeSearchDTO searchDTO, Sort sort) {
        log.info("Requested Challenge list: filters: {}", searchDTO);
        return challengeRepository.findAll(ChallengeRepository.Specifications.filter(searchDTO), sort);
    }

    @Override
    public Challenge findOne(UUID challengeId) {
        log.info("Requested Challenge {}", challengeId);
        return challengeRepository.findById(challengeId).orElseThrow();
    }
}
