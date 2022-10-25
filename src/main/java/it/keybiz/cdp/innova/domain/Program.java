package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_program")
public class Program extends SynchronizableEntity {
    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String descriptionHtml;

    @Column
    private String url;

    @Column
    private boolean isPublished;

    @Column
    private Date startDateUtc;

    @Column
    private Date dateCreatedUtc;

    @ManyToOne
    private Community community;

    @OneToMany(mappedBy = "program")
    private List<Challenge> challenges;
}
