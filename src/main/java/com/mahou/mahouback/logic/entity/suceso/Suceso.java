package com.mahou.mahouback.logic.entity.suceso;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mahou.mahouback.logic.entity.historia.Historia;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "suceso")
public class Suceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_id", nullable = false)
    @JsonBackReference
    private Historia historia;

    public Suceso() {}

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Historia getHistoria() { return historia; }
    public void setHistoria(Historia historia) { this.historia = historia; }
}
