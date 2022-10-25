package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_community")
public class Community extends SynchronizableEntity {
    @Column
    private String title;

    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String descriptionHtml;

    @Column
    private String url;

    @OneToMany(mappedBy = "community")
    private List<Program> programs;
}
