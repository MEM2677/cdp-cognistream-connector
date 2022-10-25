package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_role")
public class Role extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private OrganizationUnit organizationUnit;
}
