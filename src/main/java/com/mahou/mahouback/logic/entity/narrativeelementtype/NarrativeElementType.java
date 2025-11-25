package com.mahou.mahouback.logic.entity.narrativeelementtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "narrative_element_type")
public class NarrativeElementType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

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
}
