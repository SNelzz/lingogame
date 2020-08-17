package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    public Game startGame() {
        Game g = this.gameService.startGame();
        return g;
    }

    @PatchMapping("{gameId}/end")
    public Game endGame(@PathVariable UUID gameId) {
        Game g = this.gameService.endGame(gameId);
        return g;
    }
}
