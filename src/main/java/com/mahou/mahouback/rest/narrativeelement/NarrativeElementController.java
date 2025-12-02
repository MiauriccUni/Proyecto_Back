package com.mahou.mahouback.rest.narrativeelement;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElement;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElementsRepository;
import com.mahou.mahouback.logic.entity.relationship.Relationship;
import com.mahou.mahouback.logic.entity.relationship.RelationshipRepository;
import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/narrativeelement")

public class NarrativeElementController {

    @Autowired
    private NarrativeElementsRepository narrativeElementsRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;


    @GetMapping("/{history_id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NarrativeElement>> getNarrativeElements(@AuthenticationPrincipal User user, @PathVariable int history_id) {

        List<NarrativeElement> narrativeElements = narrativeElementsRepository.findAllByUsuario(user).stream().filter(
                narrativeElement -> narrativeElement.getHistory().getId() == history_id
        ).toList();

        if  (narrativeElements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(narrativeElements, HttpStatus.OK);
    }

    @GetMapping("/{element_id}/relations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Relationship>> getNarrativeElementRelations(@AuthenticationPrincipal User user, @PathVariable int element_id) {

        List<Relationship> relations = relationshipRepository.findAllByUsuario(user).stream().filter(
                r -> r.getFrom().getId() == element_id
        ).toList();

        if  (relations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(relations, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> newNarrativeElement(@RequestBody NarrativeElement narrativeElement, @AuthenticationPrincipal User usuarioLogeado) {

        narrativeElement.setUsuario(usuarioLogeado);
        NarrativeElement saved = narrativeElementsRepository.save(narrativeElement);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("/{element_id}/position")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateNodePosition(@RequestBody String position, @PathVariable int element_id) {

        NarrativeElement element =  narrativeElementsRepository.findById(element_id).orElse(null);

        if (element == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        element.setNodePosition(position);
        narrativeElementsRepository.save(element);

        return  new ResponseEntity<>(HttpStatus.OK);
    }

}
