package com.mahou.mahouback.rest.relationshiptype;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementType;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementTypeRepository;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipType;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipTypeRepository;
import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/relationshiptype")
@CrossOrigin(origins = "*")
public class RelationshipTypeController {

    @Autowired
    private RelationshipTypeRepository relationshipTypeRepository;

    @GetMapping
    public ResponseEntity<?> getRelationshipTypes(@AuthenticationPrincipal User user) {

        List<RelationshipType> relationshipTypeTypes = relationshipTypeRepository.findAllByUsuario(user);

        if  (relationshipTypeTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(relationshipTypeTypes, HttpStatus.OK);

    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> newRelationShipType(@AuthenticationPrincipal User user, @RequestBody RelationshipType relationshipType) {

        relationshipType.setUsuario(user);
        RelationshipType type = relationshipTypeRepository.save(relationshipType);

        return new ResponseEntity<>(type, HttpStatus.CREATED);
    }

}