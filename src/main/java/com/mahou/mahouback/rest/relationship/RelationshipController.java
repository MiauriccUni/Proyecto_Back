package com.mahou.mahouback.rest.relationship;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElement;
import com.mahou.mahouback.logic.entity.relationship.Relationship;
import com.mahou.mahouback.logic.entity.relationship.RelationshipRepository;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipTypeRepository;
import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationship")

public class RelationshipController {

    @Autowired
    private RelationshipRepository relationshipRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Relationship>> getRelationships(@AuthenticationPrincipal User usuarioLogueado){

        List<Relationship> relations = relationshipRepository.findAllByUsuario(usuarioLogueado);

        if  (relations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(relations, HttpStatus.OK);
    }

}
