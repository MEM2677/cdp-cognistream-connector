package it.keybiz.cdp.innova.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_challenge")
public class Challenge extends SynchronizableEntity {
    @Column
    private String stageId;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String descriptionHtml;

    @Column
    private int contributionCount;

    @Column
    private int type;

    @ManyToOne
    private Program program;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.EAGER)
    private List<Contribution> contributions;
}
