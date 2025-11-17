package com.mahou.mahouback.service;

import com.mahou.mahouback.client.GeminiAIClient;
import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.objeto.Objeto;
import com.mahou.mahouback.logic.entity.objeto.ObjetoRepository;
import com.mahou.mahouback.logic.entity.personaje.Personaje;
import com.mahou.mahouback.logic.entity.personaje.PersonajeRepository;
import com.mahou.mahouback.logic.entity.suceso.Suceso;
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

    public void analizarHistoria(Historia historia) {
        String contenido = historia.getContent();
        if (contenido == null || contenido.isBlank()) {
            System.out.println("La historia está vacía. No se ejecuta análisis.");
            return;
        }

        // 1. Gemini analiza el contenido
        AnalisisResponse response = geminiAIClient.enviarTextoAGemini(contenido);

        if (response == null) {
            System.out.println("⚠ No se recibió respuesta válida de Gemini.");
            return;
        }

        // 2. Guardar PERSONAJES
        response.getPersonajes().forEach(dto -> {
            Personaje p = personajeMapper.toEntity(dto);
            personajeRepository.save(p);
        });

        // 3. Guardar SUCESOS
        response.getSucesos().forEach(dto -> {
            Suceso s = sucesoMapper.toEntity(dto);
            s.setHistoria(historia);   // 🔥 IMPORTANTE
            sucesoRepository.save(s);
        });

        // 4. Guardar OBJETOS
        response.getObjetos().forEach(dto -> {
            Objeto o = objetoMapper.toEntity(dto);
            objetoRepository.save(o);
        });
    }
}
