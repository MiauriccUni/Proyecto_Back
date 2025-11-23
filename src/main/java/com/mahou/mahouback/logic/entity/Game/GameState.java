package com.mahou.mahouback.logic.entity.Game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<HexTile> tiles = new ArrayList<>();
    private List<GameUnit> units = new ArrayList<>();
    private int currentTurn;
    private int turn;
    private GameStatus status;
    private Integer winnerId;
}
