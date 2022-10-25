package it.keybiz.cdp.innova.service;

import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.dto.ChallengeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Validated
public interface ChallengeService {

    /**
     * Trova tutte le Challenge
     *
     * @param searchDTO DTO contenente filtri di ricerca
     * @param sort      ordinamento
     * @return lista contenente le Contribution paginate
     */
    List<Challenge> findAll(@NotNull ChallengeSearchDTO searchDTO, @NotNull Sort sort);

    /**
     * Trova tutte le Challenge paginate
     *
     * @param searchDTO DTO contenente filtri di ricerca
     * @param pageable  paginazione
     * @return lista contenente le Contribution paginate
     */
    Page<Challenge> findAllPaged(@NotNull ChallengeSearchDTO searchDTO, @NotNull Pageable pageable);

    /**
     * Trova una singola Challenge in base al suo ID
     *
     * @param challengeId ID
     * @return istanza Contribution
     */
    Challenge findOne(@NotNull UUID challengeId);
}
