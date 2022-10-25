package it.keybiz.cdp.innova.repository;

import it.keybiz.cdp.innova.domain.Role;
import it.keybiz.cdp.innova.domain.Role_;
import it.keybiz.cdp.innova.dto.RoleSearchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    class Specifications {
        public static Specification<Role> filter(RoleSearchDTO searchDTO) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (searchDTO.getName() != null) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Role_.NAME)), ("%" + searchDTO.getName() + "%").toLowerCase()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
