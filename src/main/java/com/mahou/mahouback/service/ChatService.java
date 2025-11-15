package com.mahou.mahouback.service;

import com.mahou.mahouback.logic.entity.chatAI.ChatMessage;
import com.mahou.mahouback.logic.entity.chatAI.ChatMessageRepository;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.historia.HistoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final HistoriaRepository historiaRepository;
    private final GeminiAIService geminiAIService;
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(HistoriaRepository historiaRepository,
                       GeminiAIService geminiAIService,
                       ChatMessageRepository chatMessageRepository) {
        this.historiaRepository = historiaRepository;
        this.geminiAIService = geminiAIService;
        this.chatMessageRepository = chatMessageRepository;
    }
    
    public String preguntarConContexto(Integer historiaId, Long usuarioId, String mensajeUsuario, String modo) {
        Historia historia = historiaRepository.findById(historiaId)
                .orElseThrow(() -> new IllegalArgumentException("Historia no encontrada"));

        if (!historia.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para usar esa historia como contexto");
        }

        String contexto = historia.getContent() == null ? "" : historia.getContent();

        String systemPrompt;
        if ("CREATIVE".equalsIgnoreCase(modo)) {
            systemPrompt = "Eres un asistente creativo que puede proponer ideas nuevas basadas en la historia. Marca claramente lo hipotético.";
        } else {
            systemPrompt = "Eres un asistente que responde solo con información respaldada por la historia. No inventes detalles.";
        }

        String prompt = systemPrompt + "\n\nHistoria (contexto):\n" + contexto + "\n\nPregunta:\n" + mensajeUsuario + "\n\nRespuesta:";

        // Llamada a Gemini (texto plano)
        String respuesta = geminiAIService.enviarChatPrompt(prompt);

        // Guardar mensaje en BD (opcional)
        try {
            ChatMessage msg = new ChatMessage();
            msg.setHistoria(historia);
            msg.setUser(historia.getUsuario()); // si quieres el usuario que creó la historia; para usuario distinto cambia por usuarioId
            msg.setUserMessage(mensajeUsuario);
            msg.setAssistantMessage(respuesta);
            chatMessageRepository.save(msg);
        } catch (Exception ignored) {}

        return respuesta;
    }
}
