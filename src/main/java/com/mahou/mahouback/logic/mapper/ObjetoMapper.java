package com.mahou.mahouback.logic.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahou.mahouback.logic.DTO.ObjetoDTO;
import com.mahou.mahouback.logic.entity.objeto.Objeto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObjetoMapper {
    public Objeto toEntity(ObjetoDTO dto) {
        Objeto o = new Objeto();
        o.setNombre(dto.getNombre());
        o.setTipo(dto.getTipo());
        o.setDescripcion(dto.getDescripcion());
        o.setRelaciones(toJson(dto.getRelaciones()));
        return o;
    }

    private String toJson(List<String> lista) {
        try {
            return new ObjectMapper().writeValueAsString(lista);
        } catch (Exception e) {
            return "[]";
        }
    }
}
