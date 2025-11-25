package com.mahou.mahouback.rest.GeminiAI;

import com.mahou.mahouback.client.GeminiAIClient;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.historia.HistoriaRepository;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.user.User;
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

    @PostMapping("/{idHistoria}")
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

        String respuesta = geminiAIClient.enviarMensajeChat(pregunta, historia.getContent());

        return new GlobalResponseHandler().handleResponse(
                "Respuesta generada",
                respuesta,
                HttpStatus.OK,
                request
        );
    }
}
