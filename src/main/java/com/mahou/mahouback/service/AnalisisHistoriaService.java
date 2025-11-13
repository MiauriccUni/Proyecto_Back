package com.mahou.mahouback.service;

import com.mahou.mahouback.client.GeminiAIClient;
import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import com.mahou.mahouback.logic.entity.objeto.ObjetoRepository;
import com.mahou.mahouback.logic.entity.personaje.PersonajeRepository;
import com.mahou.mahouback.logic.entity.suceso.SucesoRepository;
import com.mahou.mahouback.logic.mapper.ObjetoMapper;
import com.mahou.mahouback.logic.mapper.PersonajeMapper;
import com.mahou.mahouback.logic.mapper.SucesoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalisisHistoriaService {
    private final GeminiAIClient geminiAIClient;
    private final PersonajeMapper personajeMapper;
    private final SucesoMapper sucesoMapper;
    private final ObjetoMapper objetoMapper;
    private final PersonajeRepository personajeRepository;
    private final SucesoRepository sucesoRepository;
    private final ObjetoRepository objetoRepository;

    public AnalisisHistoriaService(
            GeminiAIClient geminiAIClient,
            PersonajeMapper personajeMapper,
            SucesoMapper sucesoMapper,
            ObjetoMapper objetoMapper,
            PersonajeRepository personajeRepository,
            SucesoRepository sucesoRepository,
            ObjetoRepository objetoRepository
    ) {
        this.geminiAIClient = geminiAIClient;
        this.personajeMapper = personajeMapper;
        this.sucesoMapper = sucesoMapper;
        this.objetoMapper = objetoMapper;
        this.personajeRepository = personajeRepository;
        this.sucesoRepository = sucesoRepository;
        this.objetoRepository = objetoRepository;
    }

    public void analizarHistoria(String texto) {
        AnalisisResponse response = geminiAIClient.enviarTextoAGemini(texto);

        response.getPersonajes().forEach(dto ->
                personajeRepository.save(personajeMapper.toEntity(dto)));

        response.getSucesos().forEach(dto ->
                sucesoRepository.save(sucesoMapper.toEntity(dto)));

        response.getObjetos().forEach(dto ->
                objetoRepository.save(objetoMapper.toEntity(dto)));
    }
}
