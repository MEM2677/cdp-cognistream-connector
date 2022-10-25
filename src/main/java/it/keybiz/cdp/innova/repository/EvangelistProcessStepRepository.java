package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.EvangelistProcessStep;
import it.keybiz.cdp.innova.domain.EvangelistProcessStep_;
import it.keybiz.cdp.innova.dto.EvangelistProcessStepSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvangelistProcessStepRepository extends JpaRepository<EvangelistProcessStep, UUID>, JpaSpecificationExecutor<EvangelistProcessStep> {
    List<EvangelistProcessStep> findAllByPositionGreaterThan(Integer position);

    class Specifications {
        public static Specification<EvangelistProcessStep> filter(EvangelistProcessStepSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getDescription() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(EvangelistProcessStep_.DESCRIPTION)), ("%" + searchDTO.getDescription() + "%").toLowerCase()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
