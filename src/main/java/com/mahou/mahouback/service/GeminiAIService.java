package com.mahou.mahouback.service;

import com.mahou.mahouback.client.GeminiAIClient;
import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class GeminiAIService {

    private final GeminiAIClient geminiClient;

    public GeminiAIService(GeminiAIClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public AnalisisResponse analizarTexto(String texto) {
        return geminiClient.enviarTextoAGemini(texto);
    }

}
