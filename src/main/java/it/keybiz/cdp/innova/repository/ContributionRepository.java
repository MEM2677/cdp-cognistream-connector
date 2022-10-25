package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Contribution;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContributionRepository extends SynchronizableEntityRepository<Contribution, UUID> {
}
