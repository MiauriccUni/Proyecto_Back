package com.mahou.mahouback.logic.DTO;

import java.util.List;

public class ObjetoDTO {
    private String nombre;
    private String tipo;
    private String descripcion;
    private List<String> relaciones;

    //getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(List<String> relaciones) {
        this.relaciones = relaciones;
    }
}
