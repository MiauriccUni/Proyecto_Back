package com.mahou.mahouback.logic.entity.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mahou.mahouback.logic.entity.historia.Historia;
import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "story_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"usuario", "sucesos", "hibernateLazyInitializer", "handler"})
    private Historia story;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties({"password", "historias", "hibernateLazyInitializer", "handler"})
    private User author;

    @Column(length = 500)
    private String synopsis;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false)
    private LocalDateTime publishedAt = LocalDateTime.now();

    @Column(nullable = false)
    private int views = 0;

    @Column(nullable = false)
    private int comments = 0;

    @Column(length = 50)
    private String genre;

    public ForumPost() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Historia getStory() { return story; }
    public void setStory(Historia story) { this.story = story; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getComments() { return comments; }
    public void setComments(int comments) { this.comments = comments; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}