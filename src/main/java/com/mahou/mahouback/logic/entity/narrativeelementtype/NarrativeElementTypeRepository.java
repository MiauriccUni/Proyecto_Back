package com.mahou.mahouback.logic.entity.narrativeelementtype;

import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NarrativeElementTypeRepository extends JpaRepository<NarrativeElementType, Integer> {
    List<NarrativeElementType> findAllByUsuario(User usuario);
}
