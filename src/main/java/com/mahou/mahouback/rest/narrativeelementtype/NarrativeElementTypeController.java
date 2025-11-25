package com.mahou.mahouback.rest.narrativeelementtype;
import com.mahou.mahouback.logic.entity.genre.Genre;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.http.Meta;
import com.mahou.mahouback.logic.entity.narrativeelement.NarrativeElementsRepository;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementType;
import com.mahou.mahouback.logic.entity.narrativeelementtype.NarrativeElementTypeRepository;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}