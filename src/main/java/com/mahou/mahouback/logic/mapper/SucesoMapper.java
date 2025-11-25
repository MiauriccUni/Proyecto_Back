package com.mahou.mahouback.logic.mapper;

import com.mahou.mahouback.logic.DTO.SucesoDTO;
import com.mahou.mahouback.logic.entity.suceso.Suceso;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SucesoMapper {
    public Suceso toEntity(SucesoDTO dto) {
        Suceso s = new Suceso();
        s.setTitulo(dto.getTitulo());
        s.setDescripcion(dto.getDescripcion());
        s.setFecha(LocalDate.parse(dto.getFecha())); // Convierte el String a LocalDate
        return s;
    }
}
