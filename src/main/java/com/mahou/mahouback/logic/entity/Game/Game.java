package com.mahou.mahouback.logic.entity.Game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String gameCode;

    @Column(nullable = false)
    private String hostPlayerId;

    private String guestPlayerId;

    @Column(nullable = false)
    private Integer currentTurn = 0;

    @Column(nullable = false)
    private Integer turnNumber = 1;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status = GameStatus.WAITING;

    private Long winnerId;

    @Column(nullable = false)
    private Integer seed;

    @Column(nullable = false)
    private Integer mapSize = 50;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameUnit> units = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public enum GameStatus {
        WAITING,
        ACTIVE,
        FINISHED
    }
}