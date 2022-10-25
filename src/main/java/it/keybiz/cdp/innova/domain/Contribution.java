package it.keybiz.cdp.innova.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "cdp_contribution")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Contribution extends SynchronizableEntity {
    @Column
    private Date dateCreatedUtc;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String uri;

    @Column
    private Integer upvoteCount;

    @Column
    private Integer downvoteCount;

    @Column
    private Integer commentCount;

    @ManyToOne(cascade = CascadeType.ALL)
    private Creator creator;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Map<String, String>> fields;

    @ManyToOne
    private Challenge challenge;
}
