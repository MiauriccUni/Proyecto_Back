package com.mahou.mahouback.logic.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahou.mahouback.logic.DTO.PersonajeDTO;
import com.mahou.mahouback.logic.entity.personaje.Personaje;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonajeMapper {
    public Personaje toEntity(PersonajeDTO personajeDTO){
        Personaje personaje = new Personaje();
        personaje.setNombre(personajeDTO.getNombre());
        personaje.setApellido(personajeDTO.getApellido());
        personaje.setEntidadOGrupo(personajeDTO.getEntidadOGrupo());
        personaje.setPaisOLugarOrigen(personajeDTO.getPaisOLugarOrigen());
        personaje.setDescripcion(personajeDTO.getDescripcion());
        personaje.setEstado(personajeDTO.getEstado());

        // Convertimos listas a JSON string
        personaje.setFamiliares(toJson(personajeDTO.getFamiliares()));
        personaje.setAmistades(toJson(personajeDTO.getAmistades()));
        personaje.setEnemigos(toJson(personajeDTO.getEnemigos()));

        return personaje;
    }

    private String toJson(List<String> lista) {
        if (lista == null) return "[]";
        try {
            return new ObjectMapper().writeValueAsString(lista);
        } catch (Exception e) {
            return "[]";
        }
    }
}