package com.mahou.mahouback.logic.entity.narrativeelement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementType;
import com.mahou.mahouback.logic.entity.relationship.Relationship;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipType;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "narrative_element")
public class NarrativeElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type", nullable = false)
    private NarrativeElementType type;

    @OneToMany(cascade=ALL, mappedBy="from")
    @JsonIgnore
    private Set<Relationship> relations;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_id", nullable = false)
    @JsonIgnore
    private User history;

    @Column
    private String nodePosition;


    public Set<Relationship> getRelations() {
        return relations;
    }

    public void setRelations(Set<Relationship> relations) {
        this.relations = relations;
    }

    public NarrativeElementType getType() {
        return type;
    }

    public void setType(NarrativeElementType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public User getHistory() {
        return history;
    }

    public void setHistory(User history) {
        this.history = history;
    }

    public String getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(String nodePosition) {
        this.nodePosition = nodePosition;
    }
}
