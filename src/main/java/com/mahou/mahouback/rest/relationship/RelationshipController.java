package com.mahou.mahouback.rest.relationship;
import com.mahou.mahouback.logic.DTO.RelationshipDTO;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElement;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElementsRepository;
import com.mahou.mahouback.logic.entity.relationship.Relationship;
import com.mahou.mahouback.logic.entity.relationship.RelationshipRepository;
import com.mahou.mahouback.logic.entity.relationshiptype.RelationshipType;
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
    @Autowired
    private NarrativeElementsRepository narrativeElementsRepository;

    @Autowired
    private RelationshipTypeRepository relationshipTypeRepository;


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Relationship>> getRelationships(@AuthenticationPrincipal User usuarioLogueado){

        List<Relationship> relations = relationshipRepository.findAllByUsuario(usuarioLogueado);

        if  (relations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(relations, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addRelationship(@AuthenticationPrincipal User usuarioLogueado,
                                                              @RequestBody RelationshipDTO relationship){

        NarrativeElement fromElement = narrativeElementsRepository.findById(relationship.from).orElse(null);
        NarrativeElement toElement = narrativeElementsRepository.findById(relationship.to).orElse(null);
        RelationshipType relType = relationshipTypeRepository.findById(relationship.type).orElse(null);


        Relationship relationshipEntity = new Relationship();
        relationshipEntity.setUsuario(usuarioLogueado);
        relationshipEntity.setFrom(fromElement);
        relationshipEntity.setTo(toElement);
        relationshipEntity.setType(relType);

        relationshipRepository.save(relationshipEntity);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
