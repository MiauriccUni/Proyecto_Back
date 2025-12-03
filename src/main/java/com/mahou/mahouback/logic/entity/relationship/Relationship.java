package com.mahou.mahouback.logic.entity.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElement;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipType;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "relationship")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type", nullable = false)
    private RelationshipType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "narrative_element_from", nullable = false)
    private NarrativeElement from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "narrative_element_to", nullable = false)
    private NarrativeElement to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;


    public NarrativeElement getFrom() {
        return from;
    }
    public void setFrom(NarrativeElement from) {
        this.from = from;
    }

    public NarrativeElement getTo() {
        return to;
    }
    public void setTo(NarrativeElement to) {
        this.to = to;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }


    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
