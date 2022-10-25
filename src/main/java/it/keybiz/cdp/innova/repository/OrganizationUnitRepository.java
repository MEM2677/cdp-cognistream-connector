package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.OrganizationUnit;
import it.keybiz.cdp.innova.domain.OrganizationUnit_;
import it.keybiz.cdp.innova.dto.OrganizationUnitSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, UUID>, JpaSpecificationExecutor<OrganizationUnit> {
    class Specifications {
        public static Specification<OrganizationUnit> filter(OrganizationUnitSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getName() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(OrganizationUnit_.NAME)), ("%" + searchDTO.getName() + "%").toLowerCase()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
