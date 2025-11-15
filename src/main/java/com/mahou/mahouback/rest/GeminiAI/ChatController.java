package com.mahou.mahouback.rest.GeminiAI;

import com.mahou.mahouback.client.GeminiAIClient;
import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.historia.HistoriaRepository;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.user.User;
import com.mahou.mahouback.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final HistoriaRepository historiaRepository;
    private final GeminiAIClient geminiAIClient;

    public ChatController(HistoriaRepository historiaRepository, GeminiAIClient geminiAIClient) {
        this.historiaRepository = historiaRepository;
        this.geminiAIClient = geminiAIClient;
    }

    @PostMapping("/historias/{idHistoria}")
    public ResponseEntity<?> chatConHistoria(
            @PathVariable Integer idHistoria,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User usuario,
            HttpServletRequest request
    ) {
        String pregunta = body.get("pregunta");

        Historia historia = historiaRepository.findById(idHistoria)
                .filter(h -> h.getUsuario().getId().equals(usuario.getId()))
                .orElse(null);

        if (historia == null) {
            return new GlobalResponseHandler().handleResponse(
                    "No tienes acceso a esta historia",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        // contextualizar pregunta
        String prompt = """
Eres un asistente experto para analizar historias creadas por el usuario.

Contexto de la historia:
--------------------------------
%s
--------------------------------

Pregunta del usuario:
%s

Responde siempre de forma clara, organizada y usando la historia como referencia.
""".formatted(historia.getContent(), pregunta);

        AnalisisResponse respuesta = geminiAIClient.enviarTextoAGemini(prompt);

        return new GlobalResponseHandler().handleResponse(
                "Respuesta generada",
                respuesta,
                HttpStatus.OK,
                request
        );
    }
}
