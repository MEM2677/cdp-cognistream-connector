package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class SynchronizableEntity extends AbstractAuditingEntity {
    @Id
    private UUID id;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Map<String, String>> image;

    @Column
    private boolean toDelete;
}
