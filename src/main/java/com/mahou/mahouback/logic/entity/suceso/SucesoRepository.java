package com.mahou.mahouback.logic.entity.suceso;

import com.mahou.mahouback.logic.entity.historia.Historia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SucesoRepository extends JpaRepository<Suceso, Integer> {

    // Obtener todos los sucesos de una historia específica (ordenados por fecha)
    List<Suceso> findByHistoriaOrderByFechaAsc(Historia historia);

    // Obtener un suceso por ID y por historia (para validar propiedad)
    Optional<Suceso> findByIdAndHistoria(Integer id, Historia historia);

    // Contar sucesos de una historia
    long countByHistoria(Historia historia);

    // Buscar sucesos por título dentro de una historia
    @Query("SELECT s FROM Suceso s WHERE LOWER(s.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')) AND s.historia = :historia")
    List<Suceso> findByTituloContainingAndHistoria(String titulo, Historia historia);
}