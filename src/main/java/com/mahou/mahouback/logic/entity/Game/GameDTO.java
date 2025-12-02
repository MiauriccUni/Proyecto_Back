package com.mahou.mahouback.logic.entity.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GameDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateGameRequest {
        private String playerName;
        private Integer startingUnits = 3;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateGameResponse {
        private String gameCode;
        private Long gameId;
        private String playerName;
        private Integer playerIndex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinGameRequest {
        private String gameCode;
        private String playerName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinGameResponse {
        private Long gameId;
        private String gameCode;
        private String playerName;
        private Integer playerIndex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GameStateResponse {
        private Long gameId;
        private String gameCode;
        private String status;
        private Integer currentTurn;
        private Integer turnNumber;
        private Long winnerId;
        private Integer seed;
        private Integer mapSize;
        private List<UnitDTO> units;
        private PlayerInfo hostPlayer;
        private PlayerInfo guestPlayer;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerInfo {
        private String playerId;
        private String playerName;
        private Integer playerIndex;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitDTO {
        private String id;
        private String name;
        private String type;
        private Integer owner;
        private Integer q;
        private Integer r;
        private Integer health;
        private Integer maxHealth;
        private Integer attack;
        private Integer defense;
        private Integer movement;
        private Integer remainingMovement;
        private Boolean hasAttacked;
        private String color;
        private String icon;
        private Integer attackRange;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoveUnitRequest {
        private String unitId;
        private Integer targetQ;
        private Integer targetR;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttackRequest {
        private String attackerId;
        private String defenderId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CombatResultDTO {
        private UnitDTO attacker;
        private UnitDTO defender;
        private Integer attackerDamage;
        private Integer defenderDamage;
        private Boolean attackerDied;
        private Boolean defenderDied;
    }
}