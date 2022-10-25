package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_organization_unit")
public class OrganizationUnit extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "organizationUnit", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Role> roles;
}
