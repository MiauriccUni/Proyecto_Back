package com.mahou.mahouback.logic.entity.relationshiptype;

import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipTypeRepository extends JpaRepository<RelationshipType, Integer> {
    List<RelationshipType> findAllByUsuario(User usuario);
}
