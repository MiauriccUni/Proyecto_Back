package com.mahou.mahouback.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.genai.errors.ApiException;
import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiAIClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    private Client buildClient() {
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }
    public AnalisisResponse enviarTextoAGemini(String texto) {
        try {
            Client client = buildClient();
            String prompt = generarPrompt(texto);
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash", prompt,null
            );
            String rawTextResponse = response.text();
            return mapper.readValue(rawTextResponse, AnalisisResponse.class);
        } catch (JsonProcessingException | ApiException e) {
            throw new RuntimeException("Error al procesar la solicitud a Gemini: " + e.getMessage(), e);
        }
    }

    public String enviarMensajeChat(String prompt, String contexto) {
        try {
            Client client = buildClient();

            GenerateContentResponse response = client.models.generateContent(
              "gemini-2.5-flash", contexto+"Usa la historia proveida de contexto. Responde la siguiente pregunta realizada por el usuario para ayudarlo a mejorar su redaccion y encontrar errores y/o dar recomendaciones (ESCRIBE EL MENSAJE EN UN MENSAJE DE TEXTO PLANO, SIN CAMBIOS DE GUION, SIN LETRAS EN NEGRITA NI ITALICOS NI SUBRAYADO. EL MENSAJE DEBE DE SER FLUIDO PUES LA RESPUESTA APARECERA EN UN CONTENEDOR DE TEXTO BASICO.):"+prompt,null
            );
            return response.text();
        } catch (ApiException e) {
            throw new RuntimeException("Error en la comunicación con Gemini AI: " + e.getMessage(), e);
        }
    }


    private String generarPrompt(String texto) {
        return """
Analiza la siguiente historia y extrae la información 100% estructurada.

REGLAS:
1. No inventes datos.
2. Si algo no existe, deja el campo vacío.
3. Las fechas deben estar en formato ISO: YYYY-MM-DD.
4. Las relaciones deben ser listas de nombres literales.

FORMATO ESTRICTO:

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
}
