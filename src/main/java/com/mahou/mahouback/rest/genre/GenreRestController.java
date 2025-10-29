package com.mahou.mahouback.rest.genre;

import com.mahou.mahouback.logic.entity.genre.Genre;
import com.mahou.mahouback.logic.entity.genre.GenreRepository;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.http.Meta;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreRestController {

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllGenres(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Genre> genrePage = genreRepository.findAll(pageable);

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(genrePage.getTotalPages());
        meta.setTotalElements(genrePage.getTotalElements());
        meta.setPageNumber(genrePage.getNumber() + 1);
        meta.setPageSize(genrePage.getSize());

        String message = genrePage.isEmpty() ? "No hay géneros registrados"
                : "Géneros retrieved successfully";

        return new GlobalResponseHandler().handleResponse(
                message,
                genrePage.getContent(),
                HttpStatus.OK,
                meta
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> createGenre(@RequestBody Genre genre,
                                         Authentication authentication,
                                         HttpServletRequest request) {

        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "El nombre del género es obligatorio",
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }

        if (genre.getName().length() > 50) {
            return new GlobalResponseHandler().handleResponse(
                    "El nombre del género no puede exceder 50 caracteres",
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }

        if (genreRepository.existsByName(genre.getName())) {
            return new GlobalResponseHandler().handleResponse(
                    "Ya existe un género con ese nombre",
                    HttpStatus.CONFLICT,
                    request
            );
        }

        User user = (User) authentication.getPrincipal();
        genre.setCreatorUserId(user.getId());

        Genre savedGenre = genreRepository.save(genre);
        return new GlobalResponseHandler().handleResponse(
                "Género registrado exitosamente",
                savedGenre,
                HttpStatus.CREATED,
                request
        );
    }

    @PutMapping("/{genreId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> updateGenre(@PathVariable Long genreId,
                                         @RequestBody Genre genre,
                                         HttpServletRequest request) {

        Optional<Genre> existingGenre = genreRepository.findById(genreId);
        if (existingGenre.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Género no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        if (genre.getName() == null || genre.getName().trim().isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "El nombre del género es obligatorio",
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }

        if (genre.getName().length() > 50) {
            return new GlobalResponseHandler().handleResponse(
                    "El nombre del género no puede exceder 50 caracteres",
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }

        Optional<Genre> duplicateGenre = genreRepository.findByName(genre.getName());
        if (duplicateGenre.isPresent() && !duplicateGenre.get().getId().equals(genreId)) {
            return new GlobalResponseHandler().handleResponse(
                    "Ya existe un género con ese nombre",
                    HttpStatus.CONFLICT,
                    request
            );
        }

        Genre existing = existingGenre.get();
        existing.setName(genre.getName());

        genreRepository.save(existing);

        return new GlobalResponseHandler().handleResponse(
                "Género actualizado exitosamente",
                existing,
                HttpStatus.OK,
                request
        );
    }

    @DeleteMapping("/{genreId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> deleteGenre(@PathVariable Long genreId,
                                         HttpServletRequest request) {

        Optional<Genre> existingGenre = genreRepository.findById(genreId);
        if (existingGenre.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Género no encontrado",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        genreRepository.deleteById(genreId);
        return new GlobalResponseHandler().handleResponse(
                "Género eliminado exitosamente",
                existingGenre.get(),
                HttpStatus.OK,
                request
        );
    }
}
