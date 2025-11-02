package com.mahou.mahouback.logic.entity.historia;


import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriaRepository extends JpaRepository<Historia, Integer> {
    List<Historia> findByUsuario(User usuario); // Solo devuelve historias del usuario logeado
}
