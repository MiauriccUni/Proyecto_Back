package com.mahou.mahouback.logic.entity.Game;

import com.mahou.mahouback.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "game")
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<User> players = new ArrayList<>();
    private GameState state;
    private long createdAt;
    private GameStatus status;

}
