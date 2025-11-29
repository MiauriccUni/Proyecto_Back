package com.mahou.mahouback.rest.narrativeelementtype;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementType;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementTypeRepository;
import com.mahou.mahouback.logic.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/narrativeelementtype")
public class NarrativeElementTypeController {

    @Autowired
    private NarrativeElementTypeRepository narrativeElementTypeRepository;

    @GetMapping
    public ResponseEntity<?> getNarrativeElementTypes(@AuthenticationPrincipal User user) {

        List<NarrativeElementType> narrativeElementTypes = narrativeElementTypeRepository.findAllByUsuario(user);

        if  (narrativeElementTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(narrativeElementTypes, HttpStatus.OK);

    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> newNarrativeElementType(@AuthenticationPrincipal User user, @RequestBody NarrativeElementType narrativeElementType) {

        narrativeElementType.setUsuario(user);
        NarrativeElementType type = narrativeElementTypeRepository.save(narrativeElementType);

        return new ResponseEntity<>(type, HttpStatus.CREATED);
    }

}