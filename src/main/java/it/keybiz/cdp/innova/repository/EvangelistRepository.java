package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Evangelist;
import it.keybiz.cdp.innova.domain.Evangelist_;
import it.keybiz.cdp.innova.domain.OrganizationUnit_;
import it.keybiz.cdp.innova.domain.Role_;
import it.keybiz.cdp.innova.dto.EvangelistSearchDTO;
import it.keybiz.cdp.innova.enums.EvangelistStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvangelistRepository extends JpaRepository<Evangelist, UUID>, JpaSpecificationExecutor<Evangelist> {
    List<Evangelist> findAllByStatus(EvangelistStatus status);

    class Specifications {
        public static Specification<Evangelist> filter(EvangelistSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getName() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Evangelist_.NAME)), ("%" + searchDTO.getName() + "%").toLowerCase()));
                }

                if (searchDTO.getSurname() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Evangelist_.SURNAME)), ("%" + searchDTO.getSurname() + "%").toLowerCase()));
                }

                if (searchDTO.getStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(Evangelist_.STATUS), searchDTO.getStatus()));
                }

                if (searchDTO.getOrganizationId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(Evangelist_.ROLE).get(Role_.ORGANIZATION_UNIT).get(OrganizationUnit_.ID), searchDTO.getOrganizationId()));
                }

                if (searchDTO.getRoleId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(Evangelist_.ROLE).get(Role_.ID), searchDTO.getRoleId()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
