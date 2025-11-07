package com.mahou.mahouback.rest.personaje;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.mahou.mahouback.logic.entity.personaje.Personaje;
import com.mahou.mahouback.logic.entity.personaje.PersonajeRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    @Autowired
    private PersonajeRepository personajeRepository;

    @GetMapping
    public List<Personaje> getAllPersonajes() {
        return personajeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Personaje getPersonajeById(@PathVariable Long id) {
        return personajeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personaje no encontrado"));
    }

    @PostMapping
    public Personaje createPersonaje(@RequestBody Personaje personaje) {
        return personajeRepository.save(personaje);
    }

    @PutMapping("/{id}")
    public Personaje updatePersonaje(@PathVariable Long id, @RequestBody Personaje personaje) {
        Personaje existing = personajeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personaje no encontrado"));
        existing.setNombre(personaje.getNombre());
        existing.setApellido(personaje.getApellido());
        existing.setEntidadOGrupo(personaje.getEntidadOGrupo());
        existing.setPaisOLugarOrigen(personaje.getPaisOLugarOrigen());
        existing.setDescripcion(personaje.getDescripcion());
        existing.setEstado(personaje.getEstado());
        existing.setFamiliares(personaje.getFamiliares());
        existing.setAmistades(personaje.getAmistades());
        existing.setEnemigos(personaje.getEnemigos());
        existing.setImagen(personaje.getImagen());
        return personajeRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonaje(@PathVariable Long id) {
        if (!personajeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personaje no encontrado");
        }
        personajeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadPersonajeImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String uploadDir = "upload/";
        try {
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = dirPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            Personaje personaje = personajeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personaje no encontrado"));
            personaje.setImagen(fileName); // Guarda solo el nombre del archivo
            personajeRepository.save(personaje);
            return ResponseEntity.ok().body(java.util.Map.of("imageName", fileName));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(java.util.Map.of("error", "Error al guardar la imagen"));
        }
    }
}
