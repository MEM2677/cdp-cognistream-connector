package it.keybiz.cdp.innova.domain;

import it.keybiz.cdp.innova.enums.EvangelistStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_evangelist")
public class Evangelist extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email; // todo deve essere unique?

    @Column(nullable = false)
    private String phone;  // todo deve essere unique?

    @Column(length = 1000)
    private String reason;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EvangelistStatus status = EvangelistStatus.TO_APPROVE;

    @ManyToOne
    private Role role;
}
