package com.mahou.mahouback.logic.entity.relationshiptype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementType;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "relationship_type")
public class RelationshipType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}