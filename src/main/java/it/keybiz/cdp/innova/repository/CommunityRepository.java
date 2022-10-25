package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Community;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommunityRepository extends SynchronizableEntityRepository<Community, UUID> {

}
