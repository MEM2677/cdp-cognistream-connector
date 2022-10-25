package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Program;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramRepository extends SynchronizableEntityRepository<Program, UUID> {
}
