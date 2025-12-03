package com.mahou.mahouback.logic.entity.relationship;

import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElement;
import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
    List<Relationship> findAllByUsuario(User usuario);

    List<Relationship> findAllByFrom(NarrativeElement from);
}
