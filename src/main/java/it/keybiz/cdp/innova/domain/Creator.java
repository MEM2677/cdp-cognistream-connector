package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_creator")
public class Creator extends AbstractAuditingEntity {
    @Id
    private UUID id;

    @Column
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;
}
