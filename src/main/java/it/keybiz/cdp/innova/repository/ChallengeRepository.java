package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Challenge;
import it.keybiz.cdp.innova.domain.Challenge_;
import it.keybiz.cdp.innova.domain.Community_;
import it.keybiz.cdp.innova.domain.Program_;
import it.keybiz.cdp.innova.dto.ChallengeSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChallengeRepository extends SynchronizableEntityRepository<Challenge, UUID> {
    class Specifications {
        public static Specification<Challenge> filter(ChallengeSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getProgramId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(Challenge_.PROGRAM).get(Community_.ID), searchDTO.getProgramId()));
                }

                if (searchDTO.getCommunityId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(Challenge_.PROGRAM).get(Program_.COMMUNITY).get(Community_.ID), searchDTO.getCommunityId()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
