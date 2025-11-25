package com.mahou.mahouback.rest.forum;

import com.mahou.mahouback.logic.entity.forum.ForumPost;
import com.mahou.mahouback.logic.entity.forum.ForumPostRepository;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.historia.HistoriaRepository;
import com.mahou.mahouback.logic.entity.http.GlobalResponseHandler;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forum")
public class ForumPostController {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private HistoriaRepository historiaRepository;


    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody ForumPostDTO postDTO,
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        Optional<Historia> storyOpt = historiaRepository.findById(postDTO.getStoryId());

        if (storyOpt.isEmpty() || !storyOpt.get().getUsuario().getId().equals(loggedUser.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes publicar una historia que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Historia story = storyOpt.get();

        Optional<ForumPost> existingPost = forumPostRepository.findByStoryId(story.getId());
        if (existingPost.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Esta historia ya está publicada en el foro",
                    HttpStatus.CONFLICT,
                    request
            );
        }

        ForumPost forumPost = new ForumPost();
        forumPost.setStory(story);
        forumPost.setAuthor(loggedUser);
        forumPost.setSynopsis(postDTO.getSynopsis());
        forumPost.setGenre(postDTO.getGenre());

        boolean isPublic = postDTO.getIsPublic() != null ? postDTO.getIsPublic() : true;
        forumPost.setIsPublic(isPublic);

        forumPost.setPublishedAt(LocalDateTime.now());
        forumPost.setViews(0);
        forumPost.setComments(0);

        ForumPost saved = forumPostRepository.save(forumPost);

        return new GlobalResponseHandler().handleResponse(
                "Historia publicada exitosamente en el foro",
                saved,
                HttpStatus.CREATED,
                request
        );
    }


    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        List<ForumPost> posts;

        if (loggedUser == null) {
            posts = forumPostRepository.findByIsPublicTrueOrderByPublishedAtDesc();
        } else {
            posts = forumPostRepository.findByIsPublicTrueOrAuthorIdOrderByPublishedAtDesc(
                    Math.toIntExact(loggedUser.getId())
            );
        }

        return new GlobalResponseHandler().handleResponse(
                "Historias del foro (públicas + privadas del usuario)",
                posts,
                HttpStatus.OK,
                request
        );
    }


    @PostMapping("/publish/{storyId}")
    public ResponseEntity<?> publishStory(
            @PathVariable Integer storyId,
            @RequestBody ForumPostDTO postDTO,
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        Optional<Historia> storyOpt = historiaRepository.findById(storyId);

        if (storyOpt.isEmpty() || !storyOpt.get().getUsuario().getId().equals(loggedUser.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes publicar una historia que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        Historia story = storyOpt.get();

        Optional<ForumPost> existingPost = forumPostRepository.findByStoryId(story.getId());
        if (existingPost.isPresent()) {
            return new GlobalResponseHandler().handleResponse(
                    "Esta historia ya está publicada en el foro",
                    HttpStatus.CONFLICT,
                    request
            );
        }

        ForumPost forumPost = new ForumPost();
        forumPost.setStory(story);
        forumPost.setAuthor(loggedUser);
        forumPost.setSynopsis(postDTO.getSynopsis());
        forumPost.setGenre(postDTO.getGenre());

        boolean isPublic = postDTO.getIsPublic() != null ? postDTO.getIsPublic() : true;
        forumPost.setIsPublic(isPublic);

        forumPost.setPublishedAt(LocalDateTime.now());
        forumPost.setViews(0);
        forumPost.setComments(0);

        ForumPost saved = forumPostRepository.save(forumPost);

        return new GlobalResponseHandler().handleResponse(
                "Historia publicada exitosamente en el foro",
                saved,
                HttpStatus.CREATED,
                request
        );
    }


    @GetMapping("/public")
    public ResponseEntity<?> getPublicStories(HttpServletRequest request) {
        List<ForumPost> posts = forumPostRepository.findByIsPublicTrueOrderByPublishedAtDesc();

        return new GlobalResponseHandler().handleResponse(
                "Historias públicas del foro",
                posts,
                HttpStatus.OK,
                request
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(
            @PathVariable Integer id,
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        Optional<ForumPost> postOpt = forumPostRepository.findById(id);

        if (postOpt.isEmpty()) {
            return new GlobalResponseHandler().handleResponse(
                    "Publicación no encontrada",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        ForumPost forumPost = postOpt.get();

        boolean isOwner = loggedUser != null &&
                forumPost.getAuthor() != null &&
                forumPost.getAuthor().getId().equals(loggedUser.getId());

        if (!forumPost.getIsPublic() && !isOwner) {
            return new GlobalResponseHandler().handleResponse(
                    "Publicación no encontrada o privada",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }

        forumPost.setViews(forumPost.getViews() + 1);
        forumPostRepository.save(forumPost);

        return new GlobalResponseHandler().handleResponse(
                "Publicación obtenida exitosamente",
                forumPost,
                HttpStatus.OK,
                request
        );
    }


    @GetMapping("/my-posts")
    public ResponseEntity<?> getUserPosts(
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        List<ForumPost> posts = forumPostRepository.findByAuthorId(
                Math.toIntExact(loggedUser.getId())
        );

        return new GlobalResponseHandler().handleResponse(
                "Publicaciones del usuario",
                posts,
                HttpStatus.OK,
                request
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Integer id,
            @RequestBody ForumPostDTO postDTO,
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        Optional<ForumPost> existingPostOpt = forumPostRepository.findById(id);

        if (existingPostOpt.isEmpty() ||
                !existingPostOpt.get().getAuthor().getId().equals(loggedUser.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes editar una publicación que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        ForumPost forumPost = existingPostOpt.get();
        forumPost.setSynopsis(postDTO.getSynopsis());
        forumPost.setGenre(postDTO.getGenre());

        boolean isPublic = postDTO.getIsPublic() != null
                ? postDTO.getIsPublic()
                : forumPost.getIsPublic();
        forumPost.setIsPublic(isPublic);

        ForumPost updated = forumPostRepository.save(forumPost);

        return new GlobalResponseHandler().handleResponse(
                "Publicación actualizada exitosamente",
                updated,
                HttpStatus.OK,
                request
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Integer id,
            @AuthenticationPrincipal User loggedUser,
            HttpServletRequest request) {

        Optional<ForumPost> postOpt = forumPostRepository.findById(id);

        if (postOpt.isEmpty() ||
                !postOpt.get().getAuthor().getId().equals(loggedUser.getId())) {
            return new GlobalResponseHandler().handleResponse(
                    "No puedes eliminar una publicación que no te pertenece",
                    HttpStatus.FORBIDDEN,
                    request
            );
        }

        forumPostRepository.delete(postOpt.get());

        return new GlobalResponseHandler().handleResponse(
                "Publicación eliminada exitosamente",
                HttpStatus.OK,
                request
        );
    }


    static class ForumPostDTO {
        private Integer storyId;
        private String synopsis;
        private String genre;
        private Boolean isPublic = true;

        public Integer getStoryId() {
            return storyId;
        }

        public void setStoryId(Integer storyId) {
            this.storyId = storyId;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public Boolean getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(Boolean isPublic) {
            this.isPublic = isPublic;
        }
    }
}
