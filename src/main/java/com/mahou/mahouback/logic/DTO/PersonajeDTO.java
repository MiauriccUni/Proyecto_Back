package com.mahou.mahouback.logic.DTO;
import  java.util.List;

public class PersonajeDTO {
    private String nombre;
    private String apellido;
    private String entidadOGrupo;
    private String paisOLugarOrigen;
    private String descripcion;
    private String estado;
    private List<String> familiares;
    private List<String> amistades;
    private List<String> enemigos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEntidadOGrupo() {
        return entidadOGrupo;
    }

    public void setEntidadOGrupo(String entidadOGrupo) {
        this.entidadOGrupo = entidadOGrupo;
    }

    public String getPaisOLugarOrigen() {
        return paisOLugarOrigen;
    }

    public void setPaisOLugarOrigen(String paisOLugarOrigen) {
        this.paisOLugarOrigen = paisOLugarOrigen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getFamiliares() {
        return familiares;
    }

    public void setFamiliares(List<String> familiares) {
        this.familiares = familiares;
    }

    public List<String> getAmistades() {
        return amistades;
    }

    public void setAmistades(List<String> amistades) {
        this.amistades = amistades;
    }

    public List<String> getEnemigos() {
        return enemigos;
    }

    public void setEnemigos(List<String> enemigos) {
        this.enemigos = enemigos;
    }
}
