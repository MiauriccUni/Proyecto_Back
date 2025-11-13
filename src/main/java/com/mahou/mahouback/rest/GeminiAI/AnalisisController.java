package com.mahou.mahouback.rest.GeminiAI;

import com.mahou.mahouback.service.AnalisisHistoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analisis")
public class AnalisisController {
    private final AnalisisHistoriaService analisisHistoriaService;

    public AnalisisController(AnalisisHistoriaService analisisHistoriaService) {
        this.analisisHistoriaService = analisisHistoriaService;
    }

    @PostMapping
    public ResponseEntity<String> analizar(@RequestBody String texto) {
        analisisHistoriaService.analizarHistoria(texto);
        return ResponseEntity.ok("Análisis realizado y datos guardados.");
    }
}
