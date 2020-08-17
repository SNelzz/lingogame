package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.application.RoundService;
import nl.nielsdaalhuisen.lingogame.domain.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("game/{gameId}/round")
public class RoundController {
    @Autowired
    private RoundService roundService;

    @GetMapping("start")
    public Round startRound(@PathVariable UUID gameId) {
        return this.roundService.startRound(gameId);
    }
}
