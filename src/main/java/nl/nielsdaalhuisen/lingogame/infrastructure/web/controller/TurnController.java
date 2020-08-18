package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.TurnService;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.InvalidGuessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("game/{gameId}/round/{roundId}/turn")
public class TurnController {
    @Autowired
    private TurnService turnService;

    @GetMapping("/new")
    public Round startNewTurn(@PathVariable UUID gameId, @PathVariable Long roundId) throws GameEndedException {
        return this.turnService.startNewTurn(gameId, roundId);
    }

    @PostMapping("/{turnId}/guess")
    public Round processGuess(@PathVariable UUID gameId, @PathVariable Long roundId, @PathVariable Long turnId, @RequestBody String guess) throws GameEndedException, InvalidGuessException {
        return this.turnService.processGuess(gameId, roundId, turnId, guess);
    }
}
