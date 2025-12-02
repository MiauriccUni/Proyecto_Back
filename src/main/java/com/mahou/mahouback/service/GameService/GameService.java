package com.mahou.mahouback.service.GameService;


import com.mahou.mahouback.logic.entity.Game.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameUnitRepository unitRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String[] PLAYER_COLORS = {"#4a90d9", "#d94a4a"};
    private static final Map<String, String> UNIT_ICONS = Map.of(
            "WARRIOR", "⚔️",
            "ARCHER", "🏹",
            "CAVALRY", "🐴",
            "SETTLER", "🏠"
    );

    @Transactional
    public GameDTO.CreateGameResponse createGame(GameDTO.CreateGameRequest request) {
        String gameCode = generateGameCode();
        Integer seed = generateSeed();

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setHostPlayerId(request.getPlayerName());
        game.setSeed(seed);
        game.setStatus(Game.GameStatus.WAITING);

        game = gameRepository.save(game);


        createInitialUnits(game, 0, request.getStartingUnits());

        return new GameDTO.CreateGameResponse(
                gameCode,
                game.getId(),
                request.getPlayerName(),
                0
        );
    }

    @Transactional
    public GameDTO.JoinGameResponse joinGame(GameDTO.JoinGameRequest request) {
        Game game = gameRepository.findByGameCode(request.getGameCode())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getStatus() != Game.GameStatus.WAITING) {
            throw new RuntimeException("Game already started");
        }

        if (game.getGuestPlayerId() != null) {
            throw new RuntimeException("Game is full");
        }

        game.setGuestPlayerId(request.getPlayerName());
        game.setStatus(Game.GameStatus.ACTIVE);
        game.setStartedAt(LocalDateTime.now());

        gameRepository.save(game);


        createInitialUnits(game, 1, 3);


        broadcastGameState(game.getId());

        return new GameDTO.JoinGameResponse(
                game.getId(),
                game.getGameCode(),
                request.getPlayerName(),
                1
        );
    }

    public GameDTO.GameStateResponse getGameState(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return buildGameStateResponse(game);
    }

    @Transactional
    public void moveUnit(Long gameId, GameDTO.MoveUnitRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getStatus() != Game.GameStatus.ACTIVE) {
            throw new RuntimeException("Game is not active");
        }

        GameUnit unit = unitRepository.findByGameIdAndUnitId(gameId, request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));

        if (!unit.getOwner().equals(game.getCurrentTurn())) {
            throw new RuntimeException("Not your turn");
        }


        unit.setQ(request.getTargetQ());
        unit.setR(request.getTargetR());
        unit.setRemainingMovement(0);

        unitRepository.save(unit);

        broadcastGameState(gameId);
    }

    @Transactional
    public GameDTO.CombatResultDTO attack(Long gameId, GameDTO.AttackRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getStatus() != Game.GameStatus.ACTIVE) {
            throw new RuntimeException("Game is not active");
        }

        GameUnit attacker = unitRepository.findByGameIdAndUnitId(gameId, request.getAttackerId())
                .orElseThrow(() -> new RuntimeException("Attacker not found"));

        GameUnit defender = unitRepository.findByGameIdAndUnitId(gameId, request.getDefenderId())
                .orElseThrow(() -> new RuntimeException("Defender not found"));

        if (!attacker.getOwner().equals(game.getCurrentTurn())) {
            throw new RuntimeException("Not your turn");
        }

        if (attacker.getHasAttacked()) {
            throw new RuntimeException("Unit already attacked");
        }

        if (attacker.getOwner().equals(defender.getOwner())) {
            throw new RuntimeException("Cannot attack your own units");
        }

        int distance = calculateDistance(attacker.getQ(), attacker.getR(), defender.getQ(), defender.getR());
        if (distance > attacker.getAttackRange()) {
            throw new RuntimeException("Target out of attack range");
        }

        Random random = new Random();
        double attackRoll = 0.8 + random.nextDouble() * 0.4;
        double defenseRoll = 0.8 + random.nextDouble() * 0.4;

        int attackerDamage = (int) (attacker.getAttack() * attackRoll);
        int defenderDamage = (int) (defender.getAttack() * 0.5 * defenseRoll);

        int actualDamage = Math.max(0, attackerDamage - defender.getDefense());
        int counterDamage = Math.max(0, defenderDamage - attacker.getDefense());


        attacker.setHealth(attacker.getHealth() - counterDamage);
        attacker.setHasAttacked(true);
        defender.setHealth(defender.getHealth() - actualDamage);

        boolean attackerDied = attacker.getHealth() <= 0;
        boolean defenderDied = defender.getHealth() <= 0;


        if (attackerDied) {
            unitRepository.delete(attacker);
        } else {
            unitRepository.save(attacker);
        }

        if (defenderDied) {
            unitRepository.delete(defender);
        } else {
            unitRepository.save(defender);
        }

        long hostUnits = unitRepository.countByGameIdAndOwner(gameId, 0);
        long guestUnits = unitRepository.countByGameIdAndOwner(gameId, 1);

        if (hostUnits == 0) {
            game.setStatus(Game.GameStatus.FINISHED);
            game.setWinnerId(1L);
            game.setFinishedAt(LocalDateTime.now());
            gameRepository.save(game);
        } else if (guestUnits == 0) {
            game.setStatus(Game.GameStatus.FINISHED);
            game.setWinnerId(0L);
            game.setFinishedAt(LocalDateTime.now());
            gameRepository.save(game);
        }

        GameDTO.CombatResultDTO result = new GameDTO.CombatResultDTO(
                toUnitDTO(attacker),
                toUnitDTO(defender),
                counterDamage,
                actualDamage,
                attackerDied,
                defenderDied
        );

        broadcastGameState(gameId);

        return result;
    }

    @Transactional
    public void endTurn(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getStatus() != Game.GameStatus.ACTIVE) {
            throw new RuntimeException("Game is not active");
        }

        int nextPlayer = (game.getCurrentTurn() + 1) % 2;
        int newTurnNumber = nextPlayer == 0 ? game.getTurnNumber() + 1 : game.getTurnNumber();

        game.setCurrentTurn(nextPlayer);
        game.setTurnNumber(newTurnNumber);

        gameRepository.save(game);


        List<GameUnit> units = unitRepository.findByGameIdAndOwner(gameId, nextPlayer);
        units.forEach(unit -> {
            unit.setRemainingMovement(unit.getMovement());
            unit.setHasAttacked(false);
        });
        unitRepository.saveAll(units);

        broadcastGameState(gameId);
    }

    private void createInitialUnits(Game game, Integer owner, Integer count) {
        String[] unitTypes = {"WARRIOR", "ARCHER", "CAVALRY"};

        Map<String, Integer[]> unitStats = Map.of(
                "WARRIOR", new Integer[]{100, 25, 15, 2, 1},
                "ARCHER", new Integer[]{70, 30, 8, 2, 3},
                "CAVALRY", new Integer[]{90, 35, 10, 4, 1}
        );

        int baseQ = owner == 0 ? -5 : 5;
        int baseR = owner == 0 ? -5 : 5;

        for (int i = 0; i < count; i++) {
            String type = unitTypes[i % unitTypes.length];
            Integer[] stats = unitStats.get(type);

            GameUnit unit = new GameUnit();
            unit.setGame(game);
            unit.setUnitId(UUID.randomUUID().toString());
            unit.setName(type.charAt(0) + type.substring(1).toLowerCase());
            unit.setType(GameUnit.UnitType.valueOf(type));
            unit.setOwner(owner);
            unit.setQ(baseQ + i);
            unit.setR(baseR);
            unit.setHealth(stats[0]);
            unit.setMaxHealth(stats[0]);
            unit.setAttack(stats[1]);
            unit.setDefense(stats[2]);
            unit.setMovement(stats[3]);
            unit.setRemainingMovement(stats[3]);
            unit.setAttackRange(stats[4]);
            unit.setHasAttacked(false);
            unit.setColor(PLAYER_COLORS[owner]);
            unit.setIcon(UNIT_ICONS.get(type));

            unitRepository.save(unit);
        }
    }

    private GameDTO.GameStateResponse buildGameStateResponse(Game game) {
        List<GameUnit> units = unitRepository.findByGameId(game.getId());

        return new GameDTO.GameStateResponse(
                game.getId(),
                game.getGameCode(),
                game.getStatus().name(),
                game.getCurrentTurn(),
                game.getTurnNumber(),
                game.getWinnerId(),
                game.getSeed(),
                game.getMapSize(),
                units.stream().map(this::toUnitDTO).collect(Collectors.toList()),
                new GameDTO.PlayerInfo(game.getHostPlayerId(), game.getHostPlayerId(), 0, PLAYER_COLORS[0]),
                game.getGuestPlayerId() != null ?
                        new GameDTO.PlayerInfo(game.getGuestPlayerId(), game.getGuestPlayerId(), 1, PLAYER_COLORS[1]) : null
        );
    }

    private int calculateDistance(int q1, int r1, int q2, int r2) {
        int x1 = q1, z1 = r1, y1 = -q1 - r1;
        int x2 = q2, z2 = r2, y2 = -q2 - r2;
        return (Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2)) / 2;
    }

    private GameDTO.UnitDTO toUnitDTO(GameUnit unit) {
        return new GameDTO.UnitDTO(
                unit.getUnitId(),
                unit.getName(),
                unit.getType().name().toLowerCase(),
                unit.getOwner(),
                unit.getQ(),
                unit.getR(),
                unit.getHealth(),
                unit.getMaxHealth(),
                unit.getAttack(),
                unit.getDefense(),
                unit.getMovement(),
                unit.getRemainingMovement(),
                unit.getHasAttacked(),
                unit.getColor(),
                unit.getIcon(),
                unit.getAttackRange()
        );
    }

    private void broadcastGameState(Long gameId) {
        GameDTO.GameStateResponse state = getGameState(gameId);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, state);
    }

    private String generateGameCode() {
        String code;
        do {
            code = String.format("%06d", new Random().nextInt(999999));
        } while (gameRepository.existsByGameCode(code));
        return code;
    }

    private Integer generateSeed() {
        return 1000 + new Random().nextInt(9000);
    }
}