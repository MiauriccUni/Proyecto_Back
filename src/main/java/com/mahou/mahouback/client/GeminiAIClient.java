package com.mahou.mahouback.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    private AnalisisResponse parsearRespuesta(String rawJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // La respuesta REAL de Gemini viene anidada así:
            // { "candidates": [ { "content": { "parts": [ { "text": "{json}" } ] } } ] }

            JsonNode root = mapper.readTree(rawJson);

            String contenidoPlano =
                    root.path("candidates")
                            .path(0)
                            .path("content")
                            .path("parts")
                            .path(0)
                            .path("text")
                            .asText();

            // Ahora contenidoPlano contiene EXACTAMENTE el JSON generado por tu prompt.
            // Ej: { "personajes": [...], "sucesos": [...], "objetos": [...] }

            return mapper.readValue(contenidoPlano, AnalisisResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al parsear la respuesta de Gemini: " + e.getMessage(), e);
        }
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public AnalisisResponse enviarTextoAGemini(String texto) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", generarPrompt(texto))
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> raw = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        // EXTRAER EL JSON REAL DE LA RESPUESTA
        return parsearRespuesta(raw.getBody());
    }

    private String generarPrompt(String texto) {
        return """
Analiza la siguiente historia y extrae la información de forma 100% estructurada.

REGLAS:
1. No inventes datos que no estén en la historia.
2. Si un campo no existe, devuélvelo vacío.
3. Todas las fechas deben ser convertidas a formato ISO: YYYY-MM-DD.
4. Todas las relaciones deben ser listas de nombres literales. No devuelvas IDs.

FORMATO DE SALIDA (estricto):

{
  "personajes": [
    {
      "nombre": "",
      "apellido": "",
      "entidadOGrupo": "",
      "paisOLugarOrigen": "",
      "descripcion": "",
      "estado": "",
      "familiares": [],
      "amistades": [],
      "enemigos": []
    }
  ],

  "sucesos": [
    {
      "titulo": "",
      "descripcion": "",
      "fecha": "YYYY-MM-DD"
    }
  ],

  "objetos": [
    {
      "nombre": "",
      "tipo": "",
      "descripcion": "",
      "relaciones": []
    }
  ]
}

Aquí está la historia a analizar:
""" + texto;
    }

    public String enviarMensajeChat(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

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

        ResponseEntity<String> raw = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Extrae el texto (candidates[0].content.parts[0].text)
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(raw.getBody());
            String contenidoPlano = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            return contenidoPlano;
        } catch (Exception e) {
            throw new RuntimeException("Error al extraer texto de Gemini: " + e.getMessage(), e);
        }
    }
}
