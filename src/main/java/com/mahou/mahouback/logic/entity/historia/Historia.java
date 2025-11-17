package com.mahou.mahouback.logic.entity.historia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mahou.mahouback.logic.entity.suceso.Suceso;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "historia")
public class Historia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;


    // Relación con el usuario que creó la historia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    // Relación con los sucesos dentro de la historia
    @OneToMany(mappedBy = "historia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("fecha ASC")
    private List<Suceso> sucesos;

    public Historia() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescription() { return description; }
    public void setDescription(String descripcion) { this.description = descripcion; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public List<Suceso> getSucesos() { return sucesos; }
    public void setSucesos(List<Suceso> sucesos) { this.sucesos = sucesos; }

    public String getContent() {
        return content;
    }

    public void setContent(String contenido) {
        this.content = contenido;
    }
}