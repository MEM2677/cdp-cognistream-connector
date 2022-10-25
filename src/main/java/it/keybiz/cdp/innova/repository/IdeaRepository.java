package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, UUID> {
}
