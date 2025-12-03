package com.mahou.mahouback.logic.entity.narrativeelement;

import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NarrativeElementsRepository extends JpaRepository<NarrativeElement, Integer> {
    List<NarrativeElement> findAllByUsuario(User usuario);
}
