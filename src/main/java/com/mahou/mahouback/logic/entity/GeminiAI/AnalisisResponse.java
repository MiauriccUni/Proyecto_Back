package com.mahou.mahouback.logic.entity.GeminiAI;

import com.mahou.mahouback.logic.DTO.ObjetoDTO;
import com.mahou.mahouback.logic.DTO.PersonajeDTO;
import com.mahou.mahouback.logic.DTO.SucesoDTO;

import java.util.List;

public class AnalisisResponse {
    private List<PersonajeDTO> personajes;
    private List<SucesoDTO> sucesos;
    private List<ObjetoDTO> objetos; // para ciudades, ejércitos, criaturas, poderes, etc.

    public List<PersonajeDTO> getPersonajes() {
        return personajes;
    }

    public void setPersonajes(List<PersonajeDTO> personajes) {
        this.personajes = personajes;
    }

    public List<SucesoDTO> getSucesos() {
        return sucesos;
    }

    public void setSucesos(List<SucesoDTO> sucesos) {
        this.sucesos = sucesos;
    }

    public List<ObjetoDTO> getObjetos() {
        return objetos;
    }

    public void setObjetos(List<ObjetoDTO> objetos) {
        this.objetos = objetos;
    }
}
