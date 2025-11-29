package com.mahou.mahouback.restgame;

import com.mahou.mahouback.logic.entity.Game.GameDTO;
import com.mahou.mahouback.service.GameService.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameDTO.CreateGameResponse> createGame(@RequestBody GameDTO.CreateGameRequest request) {
        return ResponseEntity.ok(gameService.createGame(request));
    }

    @PostMapping("/join")
    public ResponseEntity<GameDTO.JoinGameResponse> joinGame(@RequestBody GameDTO.JoinGameRequest request) {
        return ResponseEntity.ok(gameService.joinGame(request));
    }

    @GetMapping("/{gameId}/state")
    public ResponseEntity<GameDTO.GameStateResponse> getGameState(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGameState(gameId));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<Void> moveUnit(@PathVariable Long gameId, @RequestBody GameDTO.MoveUnitRequest request) {
        gameService.moveUnit(gameId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gameId}/attack")
    public ResponseEntity<GameDTO.CombatResultDTO> attack(@PathVariable Long gameId, @RequestBody GameDTO.AttackRequest request) {
        return ResponseEntity.ok(gameService.attack(gameId, request));
    }

    @PostMapping("/{gameId}/end-turn")
    public ResponseEntity<Void> endTurn(@PathVariable Long gameId) {
        gameService.endTurn(gameId);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/game/{gameId}/move")
    @SendTo("/topic/game/{gameId}")
    public void moveUnitWS(@DestinationVariable Long gameId, GameDTO.MoveUnitRequest request) {
        gameService.moveUnit(gameId, request);
    }

    @MessageMapping("/game/{gameId}/attack")
    @SendTo("/topic/game/{gameId}")
    public void attackWS(@DestinationVariable Long gameId, GameDTO.AttackRequest request) {
        gameService.attack(gameId, request);
    }

    @MessageMapping("/game/{gameId}/end-turn")
    @SendTo("/topic/game/{gameId}")
    public void endTurnWS(@DestinationVariable Long gameId) {
        gameService.endTurn(gameId);
    }
}