package com.mahou.mahouback.logic.entity.personaje;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Personaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String entidadOGrupo;
    private String paisOLugarOrigen;
    private String descripcion;
    private String estado;
    private String familiares; // JSON string de IDs
    private String amistades;  // JSON string de IDs
    private String enemigos;   // JSON string de IDs
    private String imagen;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEntidadOGrupo() { return entidadOGrupo; }
    public void setEntidadOGrupo(String entidadOGrupo) { this.entidadOGrupo = entidadOGrupo; }
    public String getPaisOLugarOrigen() { return paisOLugarOrigen; }
    public void setPaisOLugarOrigen(String paisOLugarOrigen) { this.paisOLugarOrigen = paisOLugarOrigen; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFamiliares() { return familiares; }
    public void setFamiliares(String familiares) { this.familiares = familiares; }
    public String getAmistades() { return amistades; }
    public void setAmistades(String amistades) { this.amistades = amistades; }
    public String getEnemigos() { return enemigos; }
    public void setEnemigos(String enemigos) { this.enemigos = enemigos; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
