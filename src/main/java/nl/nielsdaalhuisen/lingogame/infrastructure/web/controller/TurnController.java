package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.TurnService;
import nl.nielsdaalhuisen.lingogame.domain.model.Turn;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.exception.GameEndedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("game/{gameId}/round/{roundId}/turn")
public class TurnController {
    @Autowired
    private TurnService turnService;

    @GetMapping("/new")
    public Turn startNewTurn(@PathVariable UUID gameId, @PathVariable Long roundId) throws GameEndedException {
        return this.turnService.startNewTurn(gameId, roundId);
    }
}
