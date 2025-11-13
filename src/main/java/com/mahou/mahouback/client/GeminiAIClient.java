package com.mahou.mahouback.client;

import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GeminiAIClient {
    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalisisResponse enviarTextoAGemini(String texto) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        String prompt = "Analiza la siguiente historia y extrae la información de forma 100% estructurada.\n\n" +
                "REGLAS:\n" +
                "1. No inventes datos que no estén en la historia.\n" +
                "2. Si un campo no existe, devuélvelo vacío.\n" +
                "3. Todas las fechas deben ser convertidas a formato ISO: YYYY-MM-DD.\n" +
                "4. Todas las relaciones deben ser listas de nombres literales. No devuelvas IDs.\n\n" +
                "FORMATO DE SALIDA (estricto):\n\n" +
                "{\n" +
                "  \"personajes\": [\n" +
                "    {\n" +
                "      \"nombre\": \"\",\n" +
                "      \"apellido\": \"\",\n" +
                "      \"entidadOGrupo\": \"\",\n" +
                "      \"paisOLugarOrigen\": \"\",\n" +
                "      \"descripcion\": \"\",\n" +
                "      \"estado\": \"\",\n" +
                "      \"familiares\": [],\n" +
                "      \"amistades\": [],\n" +
                "      \"enemigos\": []\n" +
                "    }\n" +
                "  ],\n\n" +
                "  \"sucesos\": [\n" +
                "    {\n" +
                "      \"titulo\": \"\",\n" +
                "      \"descripcion\": \"\",\n" +
                "      \"fecha\": \"YYYY-MM-DD\"\n" +
                "    }\n" +
                "  ],\n\n" +
                "  \"objetos\": [\n" +
                "    {\n" +
                "      \"nombre\": \"\",\n" +
                "      \"tipo\": \"\",\n" +
                "      \"descripcion\": \"\",\n" +
                "      \"relaciones\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +
                "Aquí está la historia a analizar:\n" + texto;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AnalisisResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, AnalisisResponse.class);

        return response.getBody();
    }
}
