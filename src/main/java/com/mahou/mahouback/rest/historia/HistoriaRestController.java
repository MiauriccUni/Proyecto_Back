package com.mahou.mahouback.rest.historia;

import com.mahou.mahouback.logic.entity.GeminiAI.AnalisisResponse;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.historia.HistoriaRepository;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.suceso.Suceso;
import com.mahou.mahouback.logic.entity.suceso.SucesoRepository;
import com.mahou.mahouback.logic.entity.user.User;
import com.mahou.mahouback.service.GeminiAIService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/historias")
public class HistoriaRestController {

    @Autowired
    private HistoriaRepository historiaRepository;

    @Autowired
    private SucesoRepository sucesoRepository;

    // ==================== ENDPOINTS DE HISTORIAS ====================z
    // Crear historia (solo el usuario logeado)
    @PostMapping
    public ResponseEntity<?> createHistoria(
            @RequestBody Historia historia,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        historia.setUsuario(usuarioLogeado);
        Historia saved = historiaRepository.save(historia);

        return new GlobalResponseHandler().handleResponse(
                "Historia creada exitosamente",
                saved,
                HttpStatus.CREATED,
                request
        );
    }

    // Listar historias del usuario logeado
    @GetMapping
    public ResponseEntity<?> getUserHistorias(
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        List<Historia> historias = historiaRepository.findByUsuario(usuarioLogeado);

        return new GlobalResponseHandler().handleResponse(
                "Historias del usuario",
                historias,
                HttpStatus.OK,
                request
        );
    }

    // Obtener una historia por ID (solo si pertenece al usuario)
    @GetMapping("/{id}")
    public ResponseEntity<?> getHistoriaById(
            @PathVariable Integer id,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(id);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no pertenece al usuario",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        return new GlobalResponseHandler().handleResponse(
                "Historia obtenida exitosamente",
                historia.get(),
                HttpStatus.OK,
                request
        );
    }

    // Actualizar historia
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHistoria(
            @PathVariable Integer id,
            @RequestBody Historia historiaActualizada,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(id);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes actualizar una historia que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Historia existing = historia.get();
//        existing.setTitulo(historiaActualizada.getTitulo());
//        existing.setDescripcion(historiaActualizada.getDescripcion());
        existing.setContent(historiaActualizada.getContent());

        historiaRepository.save(existing);

        return new GlobalResponseHandler().handleResponse(
                "Historia actualizada exitosamente",
                existing,
                HttpStatus.OK,
                request
        );
    }

    // Eliminar historia (solo el dueño)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistoria(
            @PathVariable Integer id,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(id);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes eliminar una historia que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        historiaRepository.delete(historia.get());

        return new GlobalResponseHandler().handleResponse(
                "Historia eliminada exitosamente",
                HttpStatus.OK,
                request
        );
    }

    // ==================== ENDPOINTS DE SUCESOS ====================

    // Crear suceso dentro de una historia
    @PostMapping("/{historiaId}/sucesos")
    public ResponseEntity<?> createSuceso(
            @PathVariable Integer historiaId,
            @RequestBody Suceso suceso,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(historiaId);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no tienes permisos",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        suceso.setHistoria(historia.get());
        Suceso saved = sucesoRepository.save(suceso);

        return new GlobalResponseHandler().handleResponse(
                "Suceso creado exitosamente",
                saved,
                HttpStatus.CREATED,
                request
        );
    }

    // Listar todos los sucesos de una historia
    @GetMapping("/{historiaId}/sucesos")
    public ResponseEntity<?> getSucesosByHistoria(
            @PathVariable Integer historiaId,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(historiaId);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no tienes permisos",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        List<Suceso> sucesos = sucesoRepository.findByHistoriaOrderByFechaAsc(historia.get());

        return new GlobalResponseHandler().handleResponse(
                "Sucesos de la historia",
                sucesos,
                HttpStatus.OK,
                request
        );
    }

    // Obtener un suceso específico
    @GetMapping("/{historiaId}/sucesos/{sucesoId}")
    public ResponseEntity<?> getSucesoById(
            @PathVariable Integer historiaId,
            @PathVariable Integer sucesoId,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(historiaId);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no tienes permisos",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Optional<Suceso> suceso = sucesoRepository.findByIdAndHistoria(sucesoId, historia.get());

        if (suceso.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Suceso no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        return new GlobalResponseHandler().handleResponse(
                "Suceso obtenido exitosamente",
                suceso.get(),
                HttpStatus.OK,
                request
        );
    }

    // Actualizar un suceso
    @PutMapping("/{historiaId}/sucesos/{sucesoId}")
    public ResponseEntity<?> updateSuceso(
            @PathVariable Integer historiaId,
            @PathVariable Integer sucesoId,
            @RequestBody Suceso sucesoActualizado,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(historiaId);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no tienes permisos",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Optional<Suceso> suceso = sucesoRepository.findByIdAndHistoria(sucesoId, historia.get());

        if (suceso.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Suceso no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        Suceso existing = suceso.get();
        existing.setTitulo(sucesoActualizado.getTitulo());
        existing.setDescripcion(sucesoActualizado.getDescripcion());
        existing.setFecha(sucesoActualizado.getFecha());

        sucesoRepository.save(existing);

        return new GlobalResponseHandler().handleResponse(
                "Suceso actualizado exitosamente",
                existing,
                HttpStatus.OK,
                request
        );
    }

    // Eliminar un suceso
    @DeleteMapping("/{historiaId}/sucesos/{sucesoId}")
    public ResponseEntity<?> deleteSuceso(
            @PathVariable Integer historiaId,
            @PathVariable Integer sucesoId,
            @AuthenticationPrincipal User usuarioLogeado,
            HttpServletRequest request) {

        Optional<Historia> historia = historiaRepository.findById(historiaId);

        if (historia.isEmpty() || !historia.get().getUsuario().getId().equals(usuarioLogeado.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "Historia no encontrada o no tienes permisos",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Optional<Suceso> suceso = sucesoRepository.findByIdAndHistoria(sucesoId, historia.get());

        if (suceso.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Suceso no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        sucesoRepository.delete(suceso.get());

        return new GlobalResponseHandler().handleResponse(
                "Suceso eliminado exitosamente",
                HttpStatus.OK,
                request
        );
    }

    //AI analysis
    @Autowired
    private GeminiAIService geminiAIService;

    @PostMapping("/analizar")
    public ResponseEntity<?> analizarTextoHistoria(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {

        String texto = requestBody.get("texto");

        AnalisisResponse analisis = geminiAIService.analizarTexto(texto);

        return new GlobalResponseHandler().handleResponse(
                "Análisis generado exitosamente",
                analisis,
                HttpStatus.OK,
                request
        );
    }


}