package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Servizio;
import it.keybiz.cdp.innova.domain.Servizio_;
import it.keybiz.cdp.innova.dto.ServiceSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Servizio, UUID>, JpaSpecificationExecutor<Servizio> {
    List<Servizio> findAllByPositionGreaterThan(Integer position);

    class Specifications {
        public static Specification<Servizio> filter(ServiceSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Servizio_.TITLE)), ("%" + searchDTO.getTitle() + "%").toLowerCase()));
                }

                if (searchDTO.getDescription() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Servizio_.DESCRIPTION)), ("%" + searchDTO.getDescription() + "%").toLowerCase()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
