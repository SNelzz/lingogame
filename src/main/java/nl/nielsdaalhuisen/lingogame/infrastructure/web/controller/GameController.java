package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.Highscore;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("game")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable UUID gameId) {
        return this.gameService.getGameById(gameId);
    }

    @GetMapping("/new")
    public Game startGame() throws GameEndedException {
        return this.gameService.startGame();
    }

    @PatchMapping("{gameId}/end")
    public Game endGame(@PathVariable UUID gameId) {
        return this.gameService.endGame(gameId);
    }

    @PostMapping("{gameId}/highscore")
    public Highscore saveHighscore(@PathVariable UUID gameId, @RequestBody String playerName) {
        return this.gameService.saveHighscore(gameId,playerName);
    }
}
